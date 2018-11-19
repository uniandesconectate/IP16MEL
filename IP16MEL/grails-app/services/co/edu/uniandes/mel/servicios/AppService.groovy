package co.edu.uniandes.mel.servicios

import co.edu.uniandes.login.Estudiante
import co.edu.uniandes.login.Equipo
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
    final static String[] CICLOS_MECANICOS = ['mq1', 'mq2', 'mq3', 'mq4', 'mq5', 'mq6', 'mq7', 'mq8', 'mq9', 'mq10', 'mq11', 'mq12', 'mq13', 'mq14']

    // Tags de los rewards que otorgan estrellas en el motor.
    final static String[] REWARDS_ESTRELLAS = ['estrella_1', 'estrella_2', 'estrella_3', 'estrella_4', 'estrella_5']

    // Tags de las pruebas en Sicua.
    final static String[] TAGS_SICUA = ['MQ01', 'MQ02', 'MQ03', 'MQ04', 'MQ05', 'MQ06', 'MQ07', 'MQ08', 'MQ09', 'MQ10', 'MQ11', 'MQ12', 'MQ13', 'MQ14',
                                        'HQ01', 'HQ02', 'HQ03', 'HQ04', 'HQ05', 'HQ06', 'HQ07', 'HQ08', 'HQ09', 'HQ10', 'HQ11', 'HQ12', 'HQ13', 'HQ14',
                                        'CQ01P01', 'CQ02P01', 'CQ03P01', 'CQ04P01', 'CQ05P01', 'CQ06P01', 'CQ07P01', 'CQ08P01', 'CQ09P01', 'CQ10P01', 'CQ11P01', 'CQ12P01', 'CQ13P01', 'CQ14P01',
                                        'CQ01P02', 'CQ02P02', 'CQ03P02', 'CQ04P02', 'CQ05P02', 'CQ06P02', 'CQ07P02', 'CQ08P02', 'CQ09P02', 'CQ10P02', 'CQ11P02', 'CQ12P02', 'CQ13P02', 'CQ14P02',
                                        'CQ01P03', 'CQ02P03', 'CQ03P03', 'CQ04P03', 'CQ05P03', 'CQ06P03', 'CQ07P03', 'CQ08P03', 'CQ09P03', 'CQ10P03', 'CQ11P03', 'CQ12P03', 'CQ13P03', 'CQ14P03']

    // Tags de las pruebas para usuarios.
    final static String[] TAGS_USU = ['MEC_01', 'COF_01', 'COM_01', 'COD_01', 'HON_01',
                                      'MEC_02', 'COF_02', 'COM_02', 'COD_02', 'HON_02',
                                      'MEC_03', 'COF_03', 'COM_03', 'COD_03', 'HON_03',
                                      'MEC_04', 'COF_04', 'COM_04', 'COD_04', 'HON_04',
                                      'MEC_05', 'COF_05', 'COM_05', 'COD_05', 'HON_05',
                                      'MEC_06', 'COF_06', 'COM_06', 'COD_06', 'HON_06',
                                      'MEC_07', 'COF_07', 'COM_07', 'COD_07', 'HON_07',
                                      'MEC_08', 'COF_08', 'COM_08', 'COD_08', 'HON_08',
                                      'MEC_09', 'COF_09', 'COM_09', 'COD_09', 'HON_09',
                                      'MEC_10', 'COF_10', 'COM_10', 'COD_10', 'HON_10',
                                      'MEC_11', 'COF_11', 'COM_11', 'COD_11', 'HON_11',
                                      'MEC_12', 'COF_12', 'COM_12', 'COD_12', 'HON_12',
                                      'MEC_13', 'COF_13', 'COM_13', 'COD_13', 'HON_13',
                                      'MEC_14', 'COF_14', 'COM_14', 'COD_14', 'HON_14']

    // Tags de las pruebas en el motor.
    final static String[] TAGS_MOTOR = ['mq1', 'cfq1', 'cmq1', 'cdq1', 'hq1',
                                      'mq2', 'cfq2', 'cmq2', 'cdq2', 'hq2',
                                      'mq3', 'cfq3', 'cmq3', 'cdq3', 'hq3',
                                      'mq4', 'cfq4', 'cmq4', 'cdq4', 'hq4',
                                      'mq5', 'cfq5', 'cmq5', 'cdq5', 'hq5',
                                      'mq6', 'cfq6', 'cmq6', 'cdq6', 'hq6',
                                      'mq7', 'cfq7', 'cmq7', 'cdq7', 'hq7',
                                      'mq8', 'cfq8', 'cmq8', 'cdq8', 'hq8',
                                      'mq9', 'cfq9', 'cmq9', 'cdq9', 'hq9',
                                      'mq10', 'cfq10', 'cmq10', 'cdq10', 'hq10',
                                      'mq11', 'cfq11', 'cmq11', 'cdq11', 'hq11',
                                      'mq12', 'cfq12', 'cmq12', 'cdq12', 'hq12',
                                      'mq13', 'cfq13', 'cmq13', 'cdq13', 'hq13',
                                      'mq14', 'cfq14', 'cmq14', 'cdq14', 'hq14']

    // Número de semanas del semestre.
    final static int NUM_SEMANAS = 14

    // Nombres de las currencies en el motor.
    final static String GEMAS = 'gemas'
    final static String MONEDAS = 'monedas'

    // Parámetro para reinicio en cero de monedas de los equipos.
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
        estudiante.equipo = new Equipo()
        estudiante.equipo.nombre = json.teams[0].name
        json.missions.each {
            if(it.tag in CICLOS_MECANICOS)
            {
                it.rewards.awarded.each { it2 ->
                    if (it2.tag in REWARDS_ESTRELLAS)
                    {
                        estudiante.monedasSemanas[it.tag.toString().substring(it.tag.toString().length() - (it.tag.toString().length() == 3 ? 1 : 2)).toInteger() - 1] = it2.currencies.monedas.quantity
                        estudiante.monedas += it2.currencies.monedas.quantity
                    }
                }
                estudiante.aporteSemanas[it.tag.toString().substring(it.tag.toString().length() - (it.tag.toString().length() == 3 ? 1 : 2)).toInteger() - 1] = it.stats.team_contributions.get(json.teams[0].tag).monedas.comparative_percentage
            }
        }

        return estudiante
    }

    /***
     * Retorna los datos de un equipo.
     * @param nombreEquipo
     * @return
     */
    Equipo traerEquipo(String nombreEquipo)
    {
        JSONElement json
        Equipo equipo
        Estudiante miembro

        json = motorService.getTeamData(APP_TOKEN, nombreEquipo.replaceAll(' ', '')).json
        equipo = new Equipo()
        equipo.nombre = json.name
        equipo.monedas = json.currencies.team_currencies.monedas.quantity
        equipo.puntos = json.currencies.player_totals.puntos.quantity
        equipo.miembros = []
        json.members.each{ mie ->
            miembro = new Estudiante()
            miembro.user = new User()
            miembro.equipo = equipo
            miembro.user.username = mie.id_in_app.replace('*', '.')
            miembro.nombre = mie.name
            miembro.puntos = mie.currencies.puntos.quantity
            miembro.gemas = mie.currencies.gemas.quantity
            miembro.medallas = mie.currencies.medallas.quantity
            miembro.monedas = mie.contributions.monedas != null ? mie.contributions.monedas : 0
            equipo.miembros.add(miembro)
        }

        return equipo
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
        Equipo equipo
        Estudiante miembro

        json = motorService.getTeams(APP_TOKEN).json
        seccion = new Seccion()
        seccion.nombre = nombreSeccion
        seccion.equipos = []
        json.teams.each{ tm ->
            if(tm.name.toString().contains(nombreSeccion))
            {
                equipo = null
                for(int k = 1; k <= 40 && equipo == null; k++)
                {
                    if(tm.name.toString().endsWith(k.toString()))
                    {
                        equipo = new Equipo()
                        equipo.seccion = seccion
                        equipo.nombre = tm.name
                        equipo.puntos = tm.currencies.player_totals.puntos.quantity
                        equipo.monedas = tm.currencies.team_currencies.monedas.quantity
                        equipo.miembros = []
                        tm.members.each{ mie ->
                            miembro = new Estudiante()
                            miembro.user = new User()
                            miembro.equipo = equipo
                            miembro.user.username = mie.id_in_app.replace('*', '.')
                            miembro.nombre = mie.name
                            miembro.puntos = mie.currencies.puntos.quantity
                            miembro.gemas = mie.currencies.gemas.quantity
                            miembro.medallas = mie.currencies.medallas.quantity
                            miembro.monedas = mie.contributions.monedas != null ? mie.contributions.monedas : 0
                            equipo.miembros.add(miembro)
                        }
                        seccion.equipos.add(equipo)
                    }
                }
            }
        }

        return seccion
    }

    /***
     * Crea un estudiante y lo asigna a un equipo.
     * @param idEstudiante
     * @param nombreEstudiante
     * @param correoEstudiante
     * @param nombreEquipo
     * @return
     * @throws ServicioException
     */
    String crearEstudiante(String idEstudiante, String nombreEstudiante, String correoEstudiante, String nombreEquipo) throws ServicioException
    {
        JSONElement json
        String mensaje

        json = motorService.createPlayer(APP_TOKEN, idEstudiante.replace('.', '*'), nombreEstudiante, correoEstudiante, nombreEquipo.replaceAll(' ', '')).json
        if(json.status == 'ok') mensaje = 'El estudiante ' + idEstudiante + ' ha sido creado y asignado al equipo ' + nombreEquipo
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
     * Crea un equipo.
     * @param nombreEquipo
     * @return
     * @throws ServicioException
     */
    String crearEquipo(String nombreEquipo) throws ServicioException
    {
        JSONElement json
        String mensaje

        json = motorService.createTeam(APP_TOKEN, nombreEquipo, nombreEquipo.replaceAll(' ', '')).json
        if(json.success) mensaje = 'El equipo ' + nombreEquipo + ' ha sido creado.'
        else throw new ServicioException('Hubo un problema al crear el equipo ' + nombreEquipo + ': ' + json.status.toString())

        return mensaje
    }

    /**
     * Asigna el jugador al equipo indicado por parámetro, sacándolo de los equipos en los que se encuentre asignado actualmente.
     * @param nombreEquipo
     * @param idEstudiante
     * @return
     * @throws ServicioException
     */
    String cambiarEquipo(String nombreEquipo, String idEstudiante) throws ServicioException
    {
        JSONElement json
        String mensaje

        json = motorService.changeTeam(APP_TOKEN, nombreEquipo.replaceAll(' ', ''), idEstudiante.replace('.', '*')).json
        if(json.success) mensaje = 'El jugador ' + idEstudiante + ' ha sido movido al equipo ' + nombreEquipo
        else throw new ServicioException('Hubo un problema al mover el jugador ' + idEstudiante + ' al equipo ' + nombreEquipo + ': ' + json.status.toString())

        return mensaje
    }

    /***
     * Elimina un equipo.
     * @param nombreEquipo
     * @return
     * @throws ServicioException
     */
    String eliminarEquipo(String nombreEquipo) throws ServicioException
    {
        JSONElement json
        String mensaje

        json = motorService.deleteTeam(APP_TOKEN, nombreEquipo.replaceAll(' ', '')).json
        if(json.status == 'This team was destroyed') mensaje = 'El equipo ' + nombreEquipo + ' ha sido eliminado.'
        else throw new ServicioException('Hubo un problema al eliminar el equipo ' + nombreEquipo + ': ' + json.status.toString())

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
        Estudiante anterior = null
        Estudiante actual = null

        if(idEstudiantes.size() != idEstudiantes.unique().size() && cantidades.get(0) > 0 && cantidades.get(1) > 0) throw new ServicioException('El grupo tiene integrantes repetidos.')
        comprar = true
        for(int i = 0; i < idEstudiantes.size() && comprar; i++)
        {
            anterior = actual
            actual = traerDatosEstudiante(idEstudiantes.get(i).replace('.', '*'))
            if(actual.gemas < cantidades.get(i)) comprar = false
            if(anterior != null) if(anterior.equipo.nombre != actual.equipo.nombre) throw new ServicioException('Los estudiantes no son del mismo equipo.')
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
     * Permite gastar monedas de un equipo.
     * @param nombreEquipo
     * @param cantidad
     * @return
     * @throws ServicioException
     */
    String gastarMonedasEquipo(String nombreEquipo, int cantidad) throws ServicioException
    {
        JSONElement json
        String mensaje

        json = motorService.spendTeamCurrencies(APP_TOKEN, MONEDAS, cantidad.toString(), nombreEquipo.replaceAll(' ', '')).json
        if(json.success) mensaje = 'Se han gastado ' + cantidad + ' monedas del equipo ' + nombreEquipo
        else if(json.status == 'Not enough currencies') throw new ServicioException('El equipo ' + nombreEquipo + ' no tiene ' + cantidad + ' monedas para gastar.')
        else throw new ServicioException('Hubo un problema al gastar ' + cantidad + ' monedas del equipo ' + nombreEquipo + ': ' + json.status.toString())

        return mensaje
    }

    /***
     * Permite reiniciar en cero las monedas de un equipo.
     * @param nombreEquipo
     * @return
     * @throws ServicioException
     */
    String reiniciarMonedasEquipo(String nombreEquipo) throws ServicioException
    {
        String mensaje
        JSONElement json

        json = motorService.removeCurrenciesFromTeam(APP_TOKEN, MONEDAS, PARM_MONEDAS, nombreEquipo.replaceAll(' ', '')).json
        if(json.success) mensaje = 'Se han reiniciado en cero las monedas del equipo ' + nombreEquipo
        else throw new ServicioException('Hubo un problema al reiniciar las monedas del equipo ' + nombreEquipo + ': ' + json.status.toString())
    }

    /***
     * Permite reiniciar en cero las monedas de todos los equipos de una sección.
     * @param nombreSeccion
     * @return
     */
    String reiniciarMonedasSeccion(String nombreSeccion)
    {
        String mensaje
        Seccion seccion

        seccion = traerSeccion(nombreSeccion)
        mensaje = 'Se han reiniciado en cero las monedas de los siguientes equipos: '
        seccion.equipos.each{
            System.out.println(reiniciarMonedasEquipo(it.nombre))
            mensaje += ' ' + it.nombre + ','
        }
        mensaje = mensaje.substring(0, mensaje.length()-1) + '.'

        return mensaje
    }
}