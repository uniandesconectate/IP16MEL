package co.edu.uniandes.mel.servicios

import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONElement

@Transactional
class AppService
{
    // Token de app MEL en el motor.
    final static String APP_TOKEN = '210fd18fe01d086fe1f6ed60f789137b'

    // Servicio para comunicarse con el motor de gamificación.
    def motorService

    /***
     * Retorna los datos para desplegar el dashboard de un estudiante:
     * 1. id
     * 2. nombre
     * 3. correo
     * 4. puntos
     * 5. gemas
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
    Object[] crearEstudiante(String idEstudiante, String nombreEstudiante, String correoEstudiante, String nombreSeccion)
    {
        JSONElement json

        json = motorService.createPlayer(APP_TOKEN, idEstudiante, nombreEstudiante, correoEstudiante, nombreSeccion.replaceAll(' ', '')).json

        [json.status]
    }

    /***
     * Elimina un estudiante.
     * @param idEstudiante
     * @return
     */
    Object[] eliminarEstudiante(String idEstudiante)
    {
        JSONElement json

        json = motorService.deletePlayer(APP_TOKEN, idEstudiante).json

        [json.status]
    }

    /***
     * Crea una sección de laboratorio.
     * @param nombreSeccion
     * @return
     */
    Object[] crearSeccion(String nombreSeccion)
    {
        JSONElement json

        json = motorService.createTeam(APP_TOKEN, nombreSeccion, nombreSeccion.replaceAll(' ', '')).json

        [json.status]
    }

    /***
     * Elimina una sección de laboratorio.
     * @param nombreSeccion
     * @return
     */
    Object[] eliminarSeccion(String nombreSeccion)
    {
        JSONElement json

        json = motorService.deleteTeam(APP_TOKEN, nombreSeccion.replaceAll(' ', '')).json

        [json.status]
    }

    /***
     * Registra los puntajes de un ciclo mecánico del estudiante.
     * @param ciclo
     * @param idEstudiante
     * @param puntajes
     * @return
     */
    Object[] registrarCicloMecanico(String ciclo, String idEstudiante, int[] puntajes)
    {
        JSONElement json
        String pjs

        pjs = ''
        puntajes.each {pjs = pjs + ',' + it.toString()}
        pjs = pjs.substring(1)
        json = motorService.completeMission(APP_TOKEN, ciclo, idEstudiante, pjs).json

        [json.status]
    }
}