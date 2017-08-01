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

    // Puntaje mínimo para otorgar el reward de un ejercicio cognitivo.
    final static int PUNTAJE_COGNITIVO = 80

    // Puntaje mínimo para otorgar el reward de un ejercicio honorífico.
    final static int PUNTAJE_HONORIFICO = 80

    // Niveles de dificultad posibles para los ejercicios cognitivos.
    enum Dificultad {FACIL, INTERMEDIA, DIFICIL}

    // Tags de los ciclos mecánicos en el motor.
    final static String[] CICLOS_MECANICOS = ['ciclo_1', 'ciclo_2', 'ciclo_3', 'ciclo_4', 'ciclo_5', 'ciclo_6', 'ciclo_7', 'ciclo_8']

    // Tags de los rewards que otorgan estrellas en el motor.
    final static String[] REWARDS_ESTRELLAS = ['estrella_1', 'estrella_2', 'estrella_3', 'estrella_4', 'estrella_5']

    // Servicio para comunicarse con el motor de gamificación.
    def motorService

    /***
     * Retorna los datos de un estudiante.
     * @param idEstudiante
     * @return
     */
    Map traerDatosEstudiante(String idEstudiante)
    {
        JSONElement json
        User estudiante
        Map respuesta = [:]

        json = motorService.getPlayerData(APP_TOKEN, idEstudiante).json
        estudiante = new User()
        estudiante.nombre = json.name
        estudiante.puntos = json.currencies.puntos.quantity
        estudiante.gemas = json.currencies.gemas.quantity
        estudiante.medallas = json.currencies.medallas.quantity
        json.missions.each {
            if(it.tag in CICLOS_MECANICOS)
            {
                it.rewards.awarded.each { it2 ->
                    if (it2.tag in REWARDS_ESTRELLAS)
                        estudiante.estrellasSemanas[it.tag.toString().substring(it.tag.toString().length()-1).toInteger() - 1] = it2.currencies.monedas.quantity
                }
                estudiante.aporteSemanas[it.tag.toString().substring(it.tag.toString().length()-1).toInteger() - 1] = it.stats.team_contributions.get(json.teams[0].tag).monedas.comparative_percentage
            }
        }
        respuesta['estudiante'] = estudiante

        return respuesta
    }

    /***
     * Retorna los datos de una facción.
     * @param nombreFaccion
     * @return
     */
    Map traerFaccion(String nombreFaccion)
    {
        JSONElement json
        Faccion faccion
        User miembro
        Map respuesta = [:]

        json = motorService.getTeamData(APP_TOKEN, nombreFaccion.replaceAll(' ', '')).json
        faccion = new Faccion()
        faccion.nombreFaccion = json.name
        faccion.monedas = json.currencies.team_currencies.monedas.quantity
        faccion.puntos = json.currencies.player_totals.puntos.quantity
        faccion.miembros = []
        json.members.each{ mie ->
            miembro = new User()
            miembro.faccion = faccion
            miembro.nombre = mie.name
            miembro.puntos = mie.currencies.puntos.quantity
            miembro.gemas = mie.currencies.gemas.quantity
            miembro.medallas = mie.currencies.medallas.quantity
            faccion.miembros.add(miembro)
        }
        respuesta['faccion'] = faccion

        return respuesta
    }

    /***
     * Retorna los datos de una sección.
     * @param nombreSeccion
     * @return
     */
    Map traerSeccion(String nombreSeccion)
    {
        JSONElement json
        Map respuesta = [:]
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
        respuesta['seccion'] = seccion

        return respuesta
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
    Map crearEstudiante(String idEstudiante, String nombreEstudiante, String correoEstudiante, String nombreFaccion) throws ServicioException
    {
        JSONElement json
        Map respuesta = [:]

        json = motorService.createPlayer(APP_TOKEN, idEstudiante, nombreEstudiante, correoEstudiante, nombreFaccion.replaceAll(' ', '')).json
        if(json.status == 'ok') respuesta['mensaje'] = 'El estudiante ha sido creado y asignado a la facción.'
        else throw new ServicioException('Hubo un problema al crear el estudiante: ' + json.status.toString())

        return respuesta
    }

    /***
     * Elimina un estudiante.
     * @param idEstudiante
     * @return
     * @throws ServicioException
     */
    Map eliminarEstudiante(String idEstudiante) throws ServicioException
    {
        JSONElement json
        Map respuesta = [:]

        json = motorService.deletePlayer(APP_TOKEN, idEstudiante).json
        if(json.success) respuesta['mensaje'] = 'El estudiante ha sido eliminado.'
        else throw new ServicioException('Hubo un problema al eliminar el estudiante: ' + json.status.toString())

        return respuesta
    }

    /***
     * Crea una facción.
     * @param nombreFaccion
     * @return
     * @throws ServicioException
     */
    Map crearFaccion(String nombreFaccion) throws ServicioException
    {
        JSONElement json
        Map respuesta = [:]

        json = motorService.createTeam(APP_TOKEN, nombreFaccion, nombreFaccion.replaceAll(' ', '')).json
        if(json.success) respuesta['mensaje'] = 'La facción ha sido creada.'
        else throw new ServicioException('Hubo un problema al crear la facción: ' + json.status.toString())

        return respuesta
    }

    /***
     * Elimina una facción.
     * @param nombreFaccion
     * @return
     * @throws ServicioException
     */
    Map eliminarFaccion(String nombreFaccion) throws ServicioException
    {
        JSONElement json
        Map respuesta = [:]

        json = motorService.deleteTeam(APP_TOKEN, nombreFaccion.replaceAll(' ', '')).json
        if(json.status == 'This team was destroyed') respuesta['mensaje'] = 'La facción ha sido eliminada.'
        else throw new ServicioException('Hubo un problema al eliminar la facción: ' + json.status.toString())

        return respuesta
    }

    /***
     * Registra los puntajes de un ciclo mecánico del estudiante.
     * @param ciclo
     * @param idEstudiante
     * @param puntajes
     * @return
     * @throws ServicioException
     */
    Map registrarCicloMecanico(String ciclo, String idEstudiante, int[] puntajes) throws ServicioException
    {
        JSONElement json
        Map respuesta = [:]
        String pjs

        pjs = ''
        puntajes.each {pjs = pjs + ',' + it.toString()}
        pjs = pjs.substring(1)
        json = motorService.completeMission(APP_TOKEN, ciclo, idEstudiante, pjs, '').json
        if(json.success) respuesta['mensaje'] = 'El ciclo mecánico ha sido registrado.'
        else throw new ServicioException('Hubo un problema al registrar el ciclo mecánico: ' + json.status.toString())

        return respuesta
    }

    /***
     * Registra un ejercicio cognitivo del estudiante.
     * @param idEstudiante
     * @param dificultad
     * @param puntaje
     * @return
     * @throws ServicioException
     */
    Map registrarEjercicioCognitivo(String idEstudiante, Dificultad dificultad, int puntaje) throws ServicioException
    {
        String idReward
        JSONElement json
        Map respuesta = [:]

        if(dificultad == Dificultad.FACIL)
            idReward = 'facil'
        else if(dificultad == Dificultad.INTERMEDIA)
            idReward = 'medio'
        else if(dificultad == Dificultad.DIFICIL)
            idReward = 'dificil'
        if(puntaje >= PUNTAJE_COGNITIVO)
        {
            json = motorService.completeMission(APP_TOKEN, 'reto', idEstudiante, '', idReward).json
            if(json.success) respuesta['mensaje'] = 'El estudiante aprobó el ejercicio y fue premiado.'
            else throw new ServicioException('Hubo un problema al registrar el ejercicio cognitivo: ' + json.status.toString())
        }
        else respuesta['mensaje'] = 'El estudiante reprobó el ejercicio.'

        return respuesta
    }

    /***
     * Registra un ejercicio honorífico del estudiante.
     * @param idEstudiante
     * @param puntaje
     * @return
     * @throws ServicioException
     */
    Map registrarEjercicioHonorifico(String idEstudiante, int puntaje) throws ServicioException
    {
        JSONElement json
        Map respuesta = [:]

        json = motorService.acceptMission(APP_TOKEN, 'honores', idEstudiante).json
        if(json.success)
        {
            if (puntaje >= PUNTAJE_HONORIFICO)
            {
                json = motorService.completeMission(APP_TOKEN, 'honores', idEstudiante, '', 'honores').json
                if(json.success) respuesta['mensaje'] = 'El estudiante aprobó el ejercicio y fue premiado.'
                else throw new ServicioException('Hubo un problema al registrar el ejercicio honorífico: ' + json.status.toString())
            }
            else respuesta['mensaje'] = 'El estudiante reprobó el ejercicio.'
        }
        else throw new ServicioException('Hubo un problema al registrar el ejercicio honorífico: ' + json.status.toString())

        return respuesta
    }
}