package co.edu.uniandes.mel.servicios

import co.edu.uniandes.login.Faccion
import co.edu.uniandes.login.Seccion
import co.edu.uniandes.login.User
import co.edu.uniandes.mel.excepciones.ServicioException
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONElement

@Transactional
class AppService
{
    // Token de app MEL en el motor.
    final static String APP_TOKEN = '210fd18fe01d086fe1f6ed60f789137b'

    // Tags de las pruebas mecánicas en el motor.
    final static String[] CICLOS_MECANICOS = ['mq1', 'mq2', 'mq3', 'mq4', 'mq5', 'mq6', 'mq7', 'mq8']

    // Tags de los rewards que otorgan estrellas en el motor.
    final static String[] REWARDS_ESTRELLAS = ['estrella_1', 'estrella_2', 'estrella_3', 'estrella_4', 'estrella_5']

    // Prefijos de las pruebas en el motor.
    final static String MECANICA = 'mq'
    final static String HONORIFICA = 'hq'
    final static String COGNITIVA_FACIL = 'cfq'
    final static String COGNITIVA_MEDIA = 'cmq'
    final static String COGNITIVA_DIFICIL = 'cdq'

    // Número de quincenas del semestre.
    int numeroQuincenas = 8

    // Servicio para comunicarse con el motor de gamificación.
    def motorService

    /***
     * Retorna los datos de un estudiante.
     * @param idEstudiante
     * @return
     */
    User traerDatosEstudiante(String idEstudiante)
    {
        JSONElement json
        User estudiante

        json = motorService.getPlayerData(APP_TOKEN, idEstudiante).json
        estudiante = new User()
        estudiante.nombre = json.name
        estudiante.puntos = json.currencies.puntos.quantity
        estudiante.gemas = json.currencies.gemas.quantity
        estudiante.medallas = json.currencies.medallas.quantity
        estudiante.faccion = new Faccion()
        estudiante.faccion.nombreFaccion = json.teams[0].name
        json.missions.each {
            if(it.tag in CICLOS_MECANICOS)
            {
                it.rewards.awarded.each { it2 ->
                    if (it2.tag in REWARDS_ESTRELLAS)
                    {
                        if(it2.tag != REWARDS_ESTRELLAS[4]) estudiante.estrellasSemanas[it.tag.toString().substring(it.tag.toString().length() - 1).toInteger() - 1] = it2.currencies.monedas.quantity - 1
                        else estudiante.estrellasSemanas[it.tag.toString().substring(it.tag.toString().length() - 1).toInteger() - 1] = it2.currencies.monedas.quantity / 2
                    }
                }
                estudiante.aporteSemanas[it.tag.toString().substring(it.tag.toString().length()-1).toInteger() - 1] = it.stats.team_contributions.get(json.teams[0].tag).monedas.comparative_percentage
            }
        }

        return estudiante
    }

    /***
     * Retorna los datos de una facción.
     * @param nombreFaccion
     * @return
     */
    Faccion traerFaccion(String nombreFaccion)
    {
        JSONElement json
        Faccion faccion
        User miembro

        json = motorService.getTeamData(APP_TOKEN, nombreFaccion.replaceAll(' ', '')).json
        faccion = new Faccion()
        faccion.nombreFaccion = json.name
        faccion.monedas = json.currencies.team_currencies.monedas.quantity
        faccion.puntos = json.currencies.player_totals.puntos.quantity
        faccion.miembros = []
        json.members.each{ mie ->
            miembro = new User()
            miembro.faccion = faccion
            miembro.username = mie.id_in_app
            miembro.nombre = mie.name
            miembro.puntos = mie.currencies.puntos.quantity
            miembro.gemas = mie.currencies.gemas.quantity
            miembro.medallas = mie.currencies.medallas.quantity
            faccion.miembros.add(miembro)
        }

        return faccion
    }

    /***
     * Retorna los datos de una sección.
     * @param nombreSeccion
     * @return
     */
    Seccion traerSeccion(String nombreSeccion)
    {
        JSONElement json
        Seccion seccion
        Faccion faccion
        User miembro

        json = motorService.getTeams(APP_TOKEN).json
        seccion = new Seccion()
        seccion.nombre = nombreSeccion
        seccion.facciones = []
        json.teams.each{ tm ->
            if(tm.name.toString().contains(nombreSeccion))
            {
                ('A'..'D').each{
                    if(tm.name.toString().contains(it))
                    {
                        faccion = new Faccion()
                        faccion.seccion = seccion
                        faccion.nombreFaccion = tm.name
                        faccion.puntos = tm.currencies.player_totals.puntos.quantity
                        faccion.monedas = tm.currencies.team_currencies.monedas.quantity
                        faccion.miembros = []
                        tm.members.each{ mie ->
                            miembro = new User()
                            miembro.faccion = faccion
                            miembro.username = mie.id_in_app
                            miembro.nombre = mie.name
                            miembro.puntos = mie.currencies.puntos.quantity
                            miembro.gemas = mie.currencies.gemas.quantity
                            miembro.medallas = mie.currencies.medallas.quantity
                            faccion.miembros.add(miembro)
                        }
                    }
                    seccion.facciones.add(faccion)
                }
            }
        }

        return seccion
    }

    /***
     * Crea un estudiante y lo asigna a una facción.
     * @param idEstudiante
     * @param nombreEstudiante
     * @param correoEstudiante
     * @param nombreFaccion
     * @return
     * @throws ServicioException
     */
    String crearEstudiante(String idEstudiante, String nombreEstudiante, String correoEstudiante, String nombreFaccion) throws ServicioException
    {
        JSONElement json
        String mensaje

        json = motorService.createPlayer(APP_TOKEN, idEstudiante, nombreEstudiante, correoEstudiante, nombreFaccion.replaceAll(' ', '')).json
        if(json.status == 'ok') mensaje = 'El estudiante ha sido creado y asignado a la facción.'
        else throw new ServicioException('Hubo un problema al crear el estudiante: ' + json.status.toString())

        return mensaje
    }

    /***
     * Elimina un estudiante.
     * @param idEstudiante
     * @return
     * @throws ServicioException
     */
    String eliminarEstudiante(String idEstudiante) throws ServicioException
    {
        JSONElement json
        String mensaje

        json = motorService.deletePlayer(APP_TOKEN, idEstudiante).json
        if(json.success) mensaje = 'El estudiante ha sido eliminado.'
        else throw new ServicioException('Hubo un problema al eliminar el estudiante: ' + json.status.toString())

        return mensaje
    }

    /***
     * Crea una facción.
     * @param nombreFaccion
     * @return
     * @throws ServicioException
     */
    String crearFaccion(String nombreFaccion) throws ServicioException
    {
        JSONElement json
        String mensaje

        json = motorService.createTeam(APP_TOKEN, nombreFaccion, nombreFaccion.replaceAll(' ', '')).json
        if(json.success) mensaje = 'La facción ha sido creada.'
        else throw new ServicioException('Hubo un problema al crear la facción: ' + json.status.toString())

        return mensaje
    }

    /***
     * Elimina una facción.
     * @param nombreFaccion
     * @return
     * @throws ServicioException
     */
    String eliminarFaccion(String nombreFaccion) throws ServicioException
    {
        JSONElement json
        String mensaje

        json = motorService.deleteTeam(APP_TOKEN, nombreFaccion.replaceAll(' ', '')).json
        if(json.status == 'This team was destroyed') mensaje = 'La facción ha sido eliminada.'
        else throw new ServicioException('Hubo un problema al eliminar la facción: ' + json.status.toString())

        return mensaje
    }

    /***
     * Registra una prueba mecánica, cognitiva u honorífica del estudiante.
     * @param prueba
     * @param idEstudiante
     * @param puntaje
     * @return
     * @throws ServicioException
     */
    String registrarPrueba(String prueba, String idEstudiante, int puntaje) throws ServicioException
    {
        JSONElement json
        String mensaje

        json = motorService.completeMission(APP_TOKEN, prueba, idEstudiante, puntaje.toString(), '').json
        if(json.success) mensaje = 'La prueba ' + prueba + ' ha sido registrada para el estudiante ' + idEstudiante
        else throw new ServicioException('Hubo un problema al registrar la prueba ' + prueba + ' para el estudiante ' + idEstudiante + ': ' + json.status.toString())

        return mensaje
    }
}