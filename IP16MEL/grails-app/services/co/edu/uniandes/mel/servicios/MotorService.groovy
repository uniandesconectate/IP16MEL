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

    // ---------------------------------------------------------------------------------------------------------------------------
    // MÉTODOS GET
    // ---------------------------------------------------------------------------------------------------------------------------

    /***
     * Retorna los datos de estado y desempeño general de un jugador.
     * @param app - Token de la aplicación a consultar.
     * @param player - Id del jugador a consultar.
     * @return
     */
    RestResponse getPlayerData(String app, String player)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.get("http://playngage.io/api/players/" + player + "?include=missions&exclude[missions]=actions,prerequisites,quests,accepted_players&filter[missions]=available,achieved&options[mission]=array,rewards_by_awarded,get_stats") { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Retorna los datos de estado y desempeño general de un equipo.
     * @param app - Token de la aplicación a consultar.
     * @param team - Id del equipo a consultar.
     * @return
     */
    RestResponse getTeamData(String app, String team)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.get("http://playngage.io/api/v2/team/" + team + "?exclude=pending&members_leaderboard=puntos") { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Retorna los mercados disponibles en una aplicación.
     * @param app - Token de la aplicación a consultar.
     * @return
     */
    RestResponse getMarkets(String app)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.get("http://playngage.io/api/virtualmarkets") { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Retorna los datos de un mercado específico para un jugador.
     * @param app - Token de la aplicación a consultar.
     * @param market - Id del mercado a consultar.
     * @param player - Id del jugador a consultar.
     * @return
     */
    RestResponse getMarketForPlayer(String app, String market, String player)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.get("http://playngage.io/api/virtualmarket/" + market + "?id_in_app=" + player + "&include_items=true") { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    // ---------------------------------------------------------------------------------------------------------------------------
    // MÉTODOS POST
    // ---------------------------------------------------------------------------------------------------------------------------

    /***
     * Crea un jugador dentro de una aplicación.
     * @param app - Token de la aplicación.
     * @param player - Id del jugador a crear.
     * @param email - Correo del jugador a crear.
     * @param team - Id del equipo al cual se agregará el jugador. Si el equipo no existe entonces es creado.
     * @return
     */
    RestResponse createPlayer(String app, String player, String email, String team)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/players?id_in_app=" + player + "&email=" + email + "&team[tag]=" + team) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Permite agregar un jugador a un equipo.
     * @param app - Token de la aplicación.
     * @param team - Id del equipo al cual se agregará el jugador.
     * @param player - Id del jugador a agregar.
     * @return
     */
    RestResponse joinTeam(String app, String team, String player)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/v2/teams/" + team + "/players/" + player) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Permite a un jugador aceptar una misión.
     * @param app - Token de la aplicación.
     * @param mission - Id de la misión que el jugador va a aceptar.
     * @param player - Id del jugador.
     * @return
     */
    RestResponse acceptMission(String app, String mission, String player)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/missions/" + mission + "/players/" + player) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Permite a un jugador rechazar una misión.
     * @param app - Token de la aplicación.
     * @param mission - Id de la misión que el jugador va a rechazar.
     * @param player - Id del jugador.
     * @return
     */
    RestResponse rejectMission(String app, String mission, String player)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/missions/" + mission + "/players/" + player + "/reject") { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Permite a un jugador completar una misión.
     * @param app - Token de la aplicación.
     * @param mission - Id de la misión que el jugador va a completar.
     * @param player - Id del jugador.
     * @param scores - Puntajes obtenido por el jugador al completar la misión, separados por comas.
     * @param rewards - Premios obtenidos por el jugador al completar la misión, separados por comas.
     * @return
     */
    RestResponse completeMission(String app, String mission, String player, String scores, String rewards)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/missions/" + mission + "/complete?id_in_app=" + player + "&scores=" + scores + "&reward_tags=" + rewards) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Permite a un jugador efectuar una compra.
     * @param app - Token de la aplicación.
     * @param payment - Id de la forma de pago.
     * @param player - Id del jugador.
     * @return
     */
    RestResponse buyItemForPlayer(String app, String payment, String player)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/payments/" + payment + "?id_in_app=" + player) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Permite a un equipo efectuar una compra.
     * @param app - Token de la aplicación.
     * @param payment - Id de la forma de pago.
     * @param team - Id del equipo.
     * @return
     */
    RestResponse buyItemForTeam(String app, String payment, String team)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/payments/" + payment + "?team=" + team) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Permite restar monedas a un jugador.
     * @param app - Token de la aplicación.
     * @param currency - Id de la moneda que se restará.
     * @param quantity - Cantidad que se restará.
     * @param player - Id del jugador.
     * @return
     */
    RestResponse removeCurrenciesFromPlayer(String app, String currency, String quantity, String player)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/currencies/remove?currencies[" + currency + "]=" + quantity + "&id_in_app=" + player) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    // ---------------------------------------------------------------------------------------------------------------------------
    // MÉTODOS PUT
    // ---------------------------------------------------------------------------------------------------------------------------

    /***
     * Permite a un jugador cambiar de equipo.
     * @param app - Token de la aplicación.
     * @param team - Id del equipo al cual se agregará el jugador.
     * @param player - Id del jugador.
     * @return
     */
    RestResponse changeTeam(String app, String team, String player)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.put("http://playngage.io/api/v2/teams/" + team + "/players/" + player + "?exclude=members,pending") { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }
}