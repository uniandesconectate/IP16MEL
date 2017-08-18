package co.edu.uniandes.mel.servicios

import co.edu.uniandes.login.Estudiante
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
    final static String[] CICLOS_MECANICOS = ['mq1', 'mq2', 'mq3', 'mq4', 'mq5', 'mq6']

    // Tags de los rewards que otorgan estrellas en el motor.
    final static String[] REWARDS_ESTRELLAS = ['estrella_1', 'estrella_2', 'estrella_3', 'estrella_4', 'estrella_5']

    // Tags de las pruebas en Sicua.
    final static String[] TAGS_SICUA = ['MQ01', 'MQ02', 'MQ03', 'MQ04', 'MQ05', 'MQ06',
                                        'HQ01', 'HQ02', 'HQ03', 'HQ04', 'HQ05', 'HQ06',
                                        'CQ01P01', 'CQ02P01', 'CQ03P01', 'CQ04P01', 'CQ05P01', 'CQ06P01',
                                        'CQ01P02', 'CQ02P02', 'CQ03P02', 'CQ04P02', 'CQ05P02', 'CQ06P02',
                                        'CQ01P03', 'CQ02P03', 'CQ03P03', 'CQ04P03', 'CQ05P03', 'CQ06P03']

    // Prefijos de las pruebas en el motor.
    final static String MECANICA = 'mq'
    final static String HONORIFICA = 'hq'
    final static String COGNITIVA_FACIL = 'cfq'
    final static String COGNITIVA_MEDIA = 'cmq'
    final static String COGNITIVA_DIFICIL = 'cdq'

    // Número de quincenas del semestre.
    final static int NUM_QUINCENAS = 6

    // Nombres de las currencies en el motor.
    final static String GEMAS = 'gemas'
    final static String MONEDAS = 'monedas'

    // Parámetro para reinicio en cero de monedas de las facciones.
    final static String PARM_MONEDAS = '1000000000'

    // Servicio para comunicarse con el motor de gamificación.
    def motorService

    /***
     * Retorna los datos de un estudiante.
     * @param idEstudiante
     * @return
     */
    Estudiante traerDatosEstudiante(String idEstudiante)
    {
        JSONElement json
        Estudiante estudiante

        json = motorService.getPlayerData(APP_TOKEN, idEstudiante.replace('.', '*')).json
        estudiante = new Estudiante()
        estudiante.user = new User()
        estudiante.user.username = idEstudiante
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
                        if(it2.tag != REWARDS_ESTRELLAS[4]) estudiante.estrellasQuincenas[it.tag.toString().substring(it.tag.toString().length() - 1).toInteger() - 1] = it2.currencies.monedas.quantity - 1
                        else estudiante.estrellasQuincenas[it.tag.toString().substring(it.tag.toString().length() - 1).toInteger() - 1] = it2.currencies.monedas.quantity / 2
                    }
                }
                estudiante.aporteQuincenas[it.tag.toString().substring(it.tag.toString().length()-1).toInteger() - 1] = it.stats.team_contributions.get(json.teams[0].tag).monedas.comparative_percentage
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
        Estudiante miembro

        json = motorService.getTeamData(APP_TOKEN, nombreFaccion.replaceAll(' ', '')).json
        faccion = new Faccion()
        faccion.nombreFaccion = json.name
        faccion.monedas = json.currencies.team_currencies.monedas.quantity
        faccion.puntos = json.currencies.player_totals.puntos.quantity
        faccion.miembros = []
        json.members.each{ mie ->
            miembro = new Estudiante()
            miembro.user = new User()
            miembro.faccion = faccion
            miembro.user.username = mie.id_in_app.replace('*', '.')
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
        Estudiante miembro

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
                            miembro = new Estudiante()
                            miembro.user = new User()
                            miembro.faccion = faccion
                            miembro.user.username = mie.id_in_app.replace('*', '.')
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

        json = motorService.createPlayer(APP_TOKEN, idEstudiante.replace('.', '*'), nombreEstudiante, correoEstudiante, nombreFaccion.replaceAll(' ', '')).json
        if(json.status == 'ok') mensaje = 'El estudiante ' + idEstudiante + ' ha sido creado y asignado a la facción ' + nombreFaccion
        else throw new ServicioException('Hubo un problema al crear el estudiante ' + idEstudiante + ': ' + json.status.toString())

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

        json = motorService.deletePlayer(APP_TOKEN, idEstudiante.replace('.', '*')).json
        if(json.success) mensaje = 'El estudiante ' + idEstudiante + ' ha sido eliminado.'
        else throw new ServicioException('Hubo un problema al eliminar el estudiante ' + idEstudiante + ': ' + json.status.toString())

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
        if(json.success) mensaje = 'La facción ' + nombreFaccion + ' ha sido creada.'
        else throw new ServicioException('Hubo un problema al crear la facción ' + nombreFaccion + ': ' + json.status.toString())

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
        if(json.status == 'This team was destroyed') mensaje = 'La facción ' + nombreFaccion + ' ha sido eliminada.'
        else throw new ServicioException('Hubo un problema al eliminar la facción ' + nombreFaccion + ': ' + json.status.toString())

        return mensaje
    }

    /***
     * Registra una prueba mecánica, cognitiva u honorífica del estudiante.
     * @param idPrueba
     * @param idEstudiante
     * @param puntaje
     * @return
     * @throws ServicioException
     */
    String registrarPrueba(String idPrueba, String idEstudiante, int puntaje) throws ServicioException
    {
        JSONElement json
        String mensaje

        if(puntaje > 0)
        {
            json = motorService.completeMission(APP_TOKEN, idPrueba, idEstudiante.replace('.', '*'), puntaje.toString(), '').json
            if (json.success) mensaje = 'La prueba ' + idPrueba + ' ha sido registrada para el estudiante ' + idEstudiante
            else throw new ServicioException('Hubo un problema al registrar la prueba ' + idPrueba + ' para el estudiante ' + idEstudiante + ': ' + json.status.toString())
        }
        else mensaje = 'La prueba ' + idPrueba + ' no se registró para el estudiante ' + idEstudiante + ' porque el puntaje es menor o igual a cero.'

        return mensaje
    }

    /***
     * Permite gastar gemas de un estudiante.
     * @param idEstudiante
     * @param cantidad
     * @return
     * @throws ServicioException
     */
    String gastarGemasEstudiante(String idEstudiante, int cantidad) throws ServicioException
    {
        JSONElement json
        String mensaje

        json = motorService.spendPlayerCurrencies(APP_TOKEN, GEMAS, cantidad.toString(), idEstudiante.replace('.', '*')).json
        if(json.success) mensaje = 'Se han gastado ' + cantidad + ' gemas del estudiante ' + idEstudiante
        else if(json.status == 'Not enough currencies to spend') throw new ServicioException('El estudiante ' + idEstudiante + ' no tiene ' + cantidad + ' gemas para gastar.')
        else throw new ServicioException('Hubo un problema al gastar ' + cantidad + ' gemas del estudiante ' + idEstudiante + ': ' + json.status.toString())

        return mensaje
    }

    /***
     * Permite gastar gemas de un grupo de estudiantes.
     * @param idEstudiantes
     * @param cantidades
     * @return
     * @throws ServicioException
     */
    String gastarGemasGrupo(ArrayList<String> idEstudiantes, ArrayList<Integer> cantidades) throws ServicioException
    {
        String mensaje
        boolean comprar

        if(idEstudiantes.size() != idEstudiantes.unique().size()) throw new ServicioException('El grupo tiene integrantes repetidos.')
        comprar = true
        for(int i = 0; i < idEstudiantes.size() && comprar; i++)
        {
            if(traerDatosEstudiante(idEstudiantes.get(i).replace('.', '*')).gemas < cantidades.get(i)) comprar = false
        }
        if(comprar)
        {
            mensaje = 'Se han gastado las gemas del grupo de la siguiente manera:'
            System.out.println('gastarGemasGrupo:')
            System.out.println(idEstudiantes.toString())
            System.out.println(cantidades.toString())
            for(int i = 0; i < idEstudiantes.size(); i++)
            {
                System.out.println(gastarGemasEstudiante(idEstudiantes.get(i), cantidades.get(i)) + ' (' + (i + 1).toString() + ' de ' + idEstudiantes.size().toString() + ')')
                mensaje += ' ' + idEstudiantes.get(i) + ' gastó ' + cantidades.get(i) + ' gemas,'
            }
            mensaje = mensaje.substring(0, mensaje.length()-1) + '.'
        }
        else throw new ServicioException('El grupo no tiene las gemas suficientes.')

        return mensaje
    }

    /***
     * Permite gastar monedas de una facción.
     * @param nombreFaccion
     * @param cantidad
     * @return
     * @throws ServicioException
     */
    String gastarMonedasFaccion(String nombreFaccion, int cantidad) throws ServicioException
    {
        JSONElement json
        String mensaje

        json = motorService.spendTeamCurrencies(APP_TOKEN, MONEDAS, cantidad.toString(), nombreFaccion.replaceAll(' ', '')).json
        if(json.success) mensaje = 'Se han gastado ' + cantidad + ' monedas de la facción ' + nombreFaccion
        else if(json.status == 'Not enough currencies') throw new ServicioException('La facción ' + nombreFaccion + ' no tiene ' + cantidad + ' monedas para gastar.')
        else throw new ServicioException('Hubo un problema al gastar ' + cantidad + ' monedas de la facción ' + nombreFaccion + ': ' + json.status.toString())

        return mensaje
    }

    /***
     * Permite reiniciar en cero las monedas de una facción.
     * @param nombreFaccion
     * @return
     * @throws ServicioException
     */
    String reiniciarMonedasFaccion(String nombreFaccion) throws ServicioException
    {
        String mensaje
        JSONElement json

        json = motorService.removeCurrenciesFromTeam(APP_TOKEN, MONEDAS, PARM_MONEDAS, nombreFaccion.replaceAll(' ', '')).json
        if(json.success) mensaje = 'Se han reiniciado en cero las monedas de la facción ' + nombreFaccion
        else throw new ServicioException('Hubo un problema al reiniciar las monedas de la facción ' + nombreFaccion + ': ' + json.status.toString())
    }

    /***
     * Permite reiniciar en cero las monedas de todas las facciones de una sección.
     * @param nombreSeccion
     * @return
     */
    String reiniciarMonedasSeccion(String nombreSeccion)
    {
        String mensaje
        Seccion seccion

        seccion = traerSeccion(nombreSeccion)
        mensaje = 'Se han reiniciado en cero las monedas de las siguientes facciones: '
        seccion.facciones.each{
            System.out.println(reiniciarMonedasFaccion(it.nombreFaccion))
            mensaje += ' ' + it.nombreFaccion + ','
        }
        mensaje = mensaje.substring(0, mensaje.length()-1) + '.'

        return mensaje
    }
}