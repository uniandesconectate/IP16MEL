package co.edu.uniandes.mel.servicios

import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONElement

@Transactional
class AppService
{
    // Token de app MEL en el motor.
    final static String APP_TOKEN = '210fd18fe01d086fe1f6ed60f789137b'

    final static int MINSCORECOG = 80
    final static int MINSCOREHON = 80

    // Niveles de dificultad posibles para los ejercicios cognitivos.
    enum Dificultad {FACIL, INTERMEDIA, DIFICIL}

    // Servicio para comunicarse con el motor de gamificación.
    def motorService

    /***
     * Retorna los datos para desplegar el dashboard de un estudiante: idEstudiante, nombreEstudiante, correoEstudiante, puntosEstudiante, gemasEstudiante, estrellasEstudiante.
     * @param idEstudiante
     * @return
     */
    Map traerDashboardEstudiante(String idEstudiante)
    {
        JSONElement json
        Map respuesta = [:]

        json = motorService.getPlayerData(APP_TOKEN, idEstudiante).json
        respuesta['idEstudiante'] = json.id_in_app
        respuesta['nombreEstudiante'] = json.name
        respuesta['correoEstudiante'] = json.email
        respuesta['puntosEstudiante'] = json.currencies.puntos.quantity
        respuesta['gemasEstudiante'] = json.currencies.gemas.quantity
        respuesta['estrellasEstudiante'] = [:]
        respuesta['estrellasEstudiante']['ciclo_1'] = 0
        respuesta['estrellasEstudiante']['ciclo_2'] = 0
        respuesta['estrellasEstudiante']['ciclo_3'] = 0
        respuesta['estrellasEstudiante']['ciclo_4'] = 0
        respuesta['estrellasEstudiante']['ciclo_5'] = 0
        respuesta['estrellasEstudiante']['ciclo_6'] = 0
        respuesta['estrellasEstudiante']['ciclo_7'] = 0
        respuesta['estrellasEstudiante']['ciclo_8'] = 0
        json.missions.each {
            if(respuesta['estrellasEstudiante'][it.tag] != null)
            {
                it.rewards.awarded.each { it2 ->
                    if(it2.tag in ['estrella_1', 'estrella_2', 'estrella_3', 'estrella_4', 'estrella_5'])
                        respuesta['estrellasEstudiante'][it.tag] = it2.currencies.monedas.quantity
                }
            }
        }

        return respuesta
    }

    /***
     * Retorna los datos para desplegar el dashboard de una sección: idSeccion, nombreSeccion, monedasSeccion.
     * @param nombreSeccion
     * @return
     */
    Map traerDashboardSeccion(String nombreSeccion)
    {
        JSONElement json
        Map respuesta = [:]

        json = motorService.getTeamData(APP_TOKEN, nombreSeccion.replaceAll(' ', '')).json
        respuesta['idSeccion'] = json.tag
        respuesta['nombreSeccion'] = json.name
        respuesta['monedasSeccion'] = json.currencies.team_currencies.monedas.quantity

        return respuesta
    }

    /***
     * Crea un estudiante y lo asigna a una sección.
     * @param idEstudiante
     * @param nombreEstudiante
     * @param correoEstudiante
     * @param nombreSeccion
     * @return
     */
    Map crearEstudiante(String idEstudiante, String nombreEstudiante, String correoEstudiante, String nombreSeccion)
    {
        JSONElement json
        Map respuesta = [:]

        json = motorService.createPlayer(APP_TOKEN, idEstudiante, nombreEstudiante, correoEstudiante, nombreSeccion.replaceAll(' ', '')).json
        respuesta['status'] = json.status

        return respuesta
    }

    /***
     * Elimina un estudiante.
     * @param idEstudiante
     * @return
     */
    Map eliminarEstudiante(String idEstudiante)
    {
        JSONElement json
        Map respuesta = [:]

        json = motorService.deletePlayer(APP_TOKEN, idEstudiante).json
        respuesta['status'] = json.status

        return respuesta
    }

    /***
     * Crea una sección de laboratorio.
     * @param nombreSeccion
     * @return
     */
    Map crearSeccion(String nombreSeccion)
    {
        JSONElement json
        Map respuesta = [:]

        json = motorService.createTeam(APP_TOKEN, nombreSeccion, nombreSeccion.replaceAll(' ', '')).json
        respuesta['success'] = json.success

        return respuesta
    }

    /***
     * Elimina una sección de laboratorio.
     * @param nombreSeccion
     * @return
     */
    Map eliminarSeccion(String nombreSeccion)
    {
        JSONElement json
        Map respuesta = [:]

        json = motorService.deleteTeam(APP_TOKEN, nombreSeccion.replaceAll(' ', '')).json
        respuesta['status'] = json.status

        return respuesta
    }

    /***
     * Registra los puntajes de un ciclo mecánico del estudiante.
     * @param ciclo
     * @param idEstudiante
     * @param puntajes
     * @return
     */
    Map registrarCicloMecanico(String ciclo, String idEstudiante, int[] puntajes)
    {
        JSONElement json
        Map respuesta = [:]
        String pjs

        pjs = ''
        puntajes.each {pjs = pjs + ',' + it.toString()}
        pjs = pjs.substring(1)
        json = motorService.completeMission(APP_TOKEN, ciclo, idEstudiante, pjs, '').json
        respuesta['status'] = json.status

        return respuesta
    }

    /***
     * Registra un ejercicio cognitivo del estudiante.
     * @param idEstudiante
     * @param dificultad
     * @param puntaje
     * @return
     */
    Map registrarEjercicioCognitivo(String idEstudiante, Dificultad dificultad, int puntaje)
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
        if(puntaje >= MINSCORECOG)
        {
            json = motorService.completeMission(APP_TOKEN, 'reto', idEstudiante, '', idReward).json
            respuesta['success'] = json.success
        }

        return respuesta
    }
}