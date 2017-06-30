package co.edu.uniandes.mel.servicios

import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONElement

/***
 * Servicio de comunicación con el motor de gamificación.
 */
@Transactional
class MotorService
{

    /***
     *
     * @param token
     * @param id_in_app
     * @return
     */
    RestResponse getDashboardEstudiante(String token, String id_in_app)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/players/" + id_in_app + "?include=missions&exclude[missions]=actions,prerequisites,quests,accepted_players&filter[missions]=available,achieved&options[mission]=array,rewards_by_awarded,get_stats") { header 'Authorization', 'Token token=' + token header 'Accept', '*/*' }
        return resp
    }

    /***
     *
     * @param token
     * @param id_in_app
     * @return
     */
    RestResponse completarMision(String token, String id_in_app, String mision, String notas)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/missions/" + mision + "/complete?id_in_app=" + id_in_app + "&scores=" + notas) { header 'Authorization', 'Token token=' + token header 'Accept', '*/*' }
        return resp
    }
}