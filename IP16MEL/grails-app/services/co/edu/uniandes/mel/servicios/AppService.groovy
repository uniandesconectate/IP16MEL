package co.edu.uniandes.mel.servicios

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

    // Servicio para comunicarse con el motor de gamificación.
    def motorService

    /***
     * Retorna los datos para desplegar el dashboard de un estudiante: idEstudiante, nombreEstudiante, correoEstudiante, puntosEstudiante, gemasEstudiante,
     * medallasEstudiante, estrellasEstudiante[<ciclo_1> |...| <ciclo_8>], aporteEstudiante[<ciclo_1> |...| <ciclo_8>].
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
        respuesta['medallasEstudiante'] = json.currencies.medallas.quantity
        respuesta['estrellasEstudiante'] = [:]
        respuesta['estrellasEstudiante']['ciclo_1'] = 0
        respuesta['estrellasEstudiante']['ciclo_2'] = 0
        respuesta['estrellasEstudiante']['ciclo_3'] = 0
        respuesta['estrellasEstudiante']['ciclo_4'] = 0
        respuesta['estrellasEstudiante']['ciclo_5'] = 0
        respuesta['estrellasEstudiante']['ciclo_6'] = 0
        respuesta['estrellasEstudiante']['ciclo_7'] = 0
        respuesta['estrellasEstudiante']['ciclo_8'] = 0
        respuesta['aporteEstudiante'] = [:]
        respuesta['aporteEstudiante']['ciclo_1'] = 0
        respuesta['aporteEstudiante']['ciclo_2'] = 0
        respuesta['aporteEstudiante']['ciclo_3'] = 0
        respuesta['aporteEstudiante']['ciclo_4'] = 0
        respuesta['aporteEstudiante']['ciclo_5'] = 0
        respuesta['aporteEstudiante']['ciclo_6'] = 0
        respuesta['aporteEstudiante']['ciclo_7'] = 0
        respuesta['aporteEstudiante']['ciclo_8'] = 0
        json.missions.each {
            if (respuesta['estrellasEstudiante'][it.tag] != null)
            {
                it.rewards.awarded.each { it2 ->
                    if (it2.tag in ['estrella_1', 'estrella_2', 'estrella_3', 'estrella_4', 'estrella_5'])
                        respuesta['estrellasEstudiante'][it.tag] = it2.currencies.monedas.quantity
                }
                respuesta['aporteEstudiante'][it.tag] = it.stats.team_contributions.get(json.teams[0].tag).monedas.comparative_percentage
            }
        }

        return respuesta
    }

    /***
     * Retorna los datos para desplegar el dashboard de una sección: idSeccion, nombreSeccion, monedasSeccion, puntosSeccion.
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
        respuesta['puntosSeccion'] = json.currencies.player_totals.puntos.quantity

        return respuesta
    }

    /***
     * Crea un estudiante y lo asigna a una sección.
     * @param idEstudiante
     * @param nombreEstudiante
     * @param correoEstudiante
     * @param nombreSeccion
     * @return
     * @throws ServicioException
     */
    Map crearEstudiante(String idEstudiante, String nombreEstudiante, String correoEstudiante, String nombreSeccion) throws ServicioException
    {
        JSONElement json
        Map respuesta = [:]

        json = motorService.createPlayer(APP_TOKEN, idEstudiante, nombreEstudiante, correoEstudiante, nombreSeccion.replaceAll(' ', '')).json
        if(json.status == 'ok') respuesta['mensaje'] = 'El estudiante ha sido creado y asignado a la sección.'
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
     * Crea una sección de laboratorio.
     * @param nombreSeccion
     * @return
     * @throws ServicioException
     */
    Map crearSeccion(String nombreSeccion) throws ServicioException
    {
        JSONElement json
        Map respuesta = [:]

        json = motorService.createTeam(APP_TOKEN, nombreSeccion, nombreSeccion.replaceAll(' ', '')).json
        if(json.success) respuesta['mensaje'] = 'La sección ha sido creada.'
        else throw new ServicioException('Hubo un problema al crear la sección: ' + json.status.toString())

        return respuesta
    }

    /***
     * Elimina una sección de laboratorio.
     * @param nombreSeccion
     * @return
     * @throws ServicioException
     */
    Map eliminarSeccion(String nombreSeccion) throws ServicioException
    {
        JSONElement json
        Map respuesta = [:]

        json = motorService.deleteTeam(APP_TOKEN, nombreSeccion.replaceAll(' ', '')).json
        if(json.status == 'This team was destroyed') respuesta['mensaje'] = 'La sección ha sido eliminada.'
        else throw new ServicioException('Hubo un problema al eliminar la sección: ' + json.status.toString())

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