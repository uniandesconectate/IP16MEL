package co.edu.uniandes.mel.servicios

import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONElement

@Transactional
class AppService
{
    // Token de app MEL en el motor.
    final static String APP_TOKEN = '210fd18fe01d086fe1f6ed60f789137b'

    final static int[] puntajesMecanicos = [105, 180, 240, 270, 300]

    // Servicio para comunicarse con el motor de gamificación.
    def motorService

    /***
     * Agrega un estudiante a la aplicación.
     * @param id
     * @param nombre
     * @param correo
     * @param seccionLaboratorio
     * @return
     */
    Object[] crearEstudiante(String id, String nombre, String correo, String seccionLaboratorio)
    {
        JSONElement json

        json = motorService.createPlayer(APP_TOKEN, id, nombre, correo, seccionLaboratorio).json

        [json.status]
    }

    /***
     * Retorna los datos para desplegar en el dashboard del estudiante:
     * 1. id in app
     * 2. nombre
     * 3. correo
     * 4. puntos
     * 5. gemas
     * 6. Estrellas por semana
     * @param estudiante
     * @return
     */
    Object[] getDashboardEstudiante(String estudiante)
    {
        int[] estrellas
        JSONElement json

        estrellas = [0, 0, 0, 0, 0, 0, 0, 0]
        json = motorService.getPlayerData(APP_TOKEN, estudiante).json

        json.missions.available.semana_1.rewards.each{
            if(it.title == 'Una estrella' && it.given_count > 0)
                estrellas[1] = it.given_count
            else if(it.title == 'Dos estrellas' && it.given_count > 0)
                estrellas[1] = it.given_count * 2
            else if(it.title == 'Tres estrellas' && it.given_count > 0)
                estrellas[1] = it.given_count * 3
            else if(it.title == 'Cuatro estrellas' && it.given_count > 0)
                estrellas[1] = it.given_count * 4
            else if(it.title == 'Cinco estrellas' && it.given_count > 0)
                estrellas[1] = it.given_count * 5
        }

        [json.id_in_app, json.name, json.email, json.currencies.puntos.quantity, json.currencies.gemas.quantity, estrellas]
    }

    /***
     * Permite registrar el puntaje de un ciclo mecánico de un estudiante.
     * @param ciclo
     * @param estudiante
     * @param puntaje
     * @return
     */
    Object[] registrarCicloMecanico(String ciclo, String estudiante, int puntaje)
    {
        JSONElement json
        String premios

        premios = ''
        if(puntaje == puntajesMecanicos[4])
            premios = 'estrella_5,estrellas_todas'
        else if(puntaje >= puntajesMecanicos[3])
            premios = 'estrella_4'
        else if(puntaje >= puntajesMecanicos[2])
            premios = 'estrella_3'
        else if(puntaje >= puntajesMecanicos[1])
            premios = 'estrella_2'
        else if(puntaje >= puntajesMecanicos[0])
            premios = 'estrella_1'
        json = motorService.completeMission(APP_TOKEN, ciclo, estudiante, puntaje.toString(), premios).json

        [json.status]
    }

    /***
     * Elimina un estudiante de la aplicación.
     * @param estudiante
     * @return
     */
    Object[] eliminarEstudiante(String estudiante)
    {
        JSONElement json

        json = motorService.deletePlayer(APP_TOKEN, estudiante).json

        [json.status]
    }
}