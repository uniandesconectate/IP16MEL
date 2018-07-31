package co.edu.uniandes.mel.servicios

import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.transaction.Transactional

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
     * @param app
     * @param idPlayer
     * @return
     */
    RestResponse getPlayerData(String app, String idPlayer)
    {
        RestBuilder rest
        RestResponse resp

        rest = new RestBuilder()
        resp = rest.get("http://playngage.io/api/players/" + idPlayer + "?include=missions,teams&exclude[missions]=actions,prerequisites,quests,accepted_players&filters[missions]=available,achieved&options[missions]=array,rewards_by_awarded,get_stats") { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Retorna los datos de estado y desempeño general de un equipo.
     * @param app
     * @param idTeam
     * @return
     */
    RestResponse getTeamData(String app, String idTeam)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.get("http://playngage.io/api/v2/teams/" + idTeam + "?exclude=pending&members_leaderboard=puntos") { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Retorna todos los equipos que existen.
     * @param app
     * @return
     */
    RestResponse getTeams(String app)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.get("http://playngage.io/api/v2/teams") { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Retorna los mercados disponibles.
     * @param app
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
     * @param app
     * @param idMarket
     * @param idPlayer
     * @return
     */
    RestResponse getMarketForPlayer(String app, String idMarket, String idPlayer)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.get("http://playngage.io/api/virtualmarket/" + idMarket + "?id_in_app=" + idPlayer + "&include_items=true") { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    // ---------------------------------------------------------------------------------------------------------------------------
    // MÉTODOS POST
    // ---------------------------------------------------------------------------------------------------------------------------

    /***
     * Crea un jugador.
     * @param app
     * @param idPlayer
     * @param name
     * @param email
     * @param idTeam
     * @return
     */
    RestResponse createPlayer(String app, String idPlayer, String name, String email, String idTeam)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/players?id_in_app=" + idPlayer + "&email=" + email + "&name=" + name + "&team[tag]=" + idTeam) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Permite a un jugador aceptar una misión.
     * @param app
     * @param idMission
     * @param idPlayer
     * @return
     */
    RestResponse acceptMission(String app, String idMission, String idPlayer)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/missions/" + idMission + "/players/" + idPlayer + "/accept") { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Permite a un jugador rechazar una misión.
     * @param app
     * @param idMission
     * @param idPlayer
     * @return
     */
    RestResponse rejectMission(String app, String idMission, String idPlayer)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/missions/" + idMission + "/players/" + idPlayer + "/reject") { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Permite a un jugador completar una misión.
     * @param app
     * @param idMission
     * @param idPlayer
     * @param scores
     * @param rewards
     * @return
     */
    RestResponse completeMission(String app, String idMission, String idPlayer, String scores, String rewards)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/missions/" + idMission + "/complete?id_in_app=" + idPlayer + "&scores=" + scores + "&reward_tags=" + rewards) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Permite a un jugador efectuar una compra.
     * @param app
     * @param idPayment
     * @param idPlayer
     * @return
     */
    RestResponse buyItemForPlayer(String app, String idPayment, String idPlayer)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/payments/" + idPayment + "?id_in_app=" + idPlayer) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Permite a un equipo efectuar una compra.
     * @param app
     * @param idPayment
     * @param idTeam
     * @return
     */
    RestResponse buyItemForTeam(String app, String idPayment, String idTeam)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/payments/" + idPayment + "?team=" + idTeam) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Permite gastar una cantidad de un tipo de moneda de un jugador.
     * @param app
     * @param idCurrency
     * @param quantity
     * @param idPlayer
     * @return
     */
    RestResponse spendPlayerCurrencies(String app, String idCurrency, String quantity, String idPlayer)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/currencies/spend?currencies[" + idCurrency + "]=" + quantity + "&id_in_app=" + idPlayer) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Permite gastar una cantidad de un tipo de moneda de un equipo.
     * @param app
     * @param idCurrency
     * @param quantity
     * @param idTeam
     * @return
     */
    RestResponse spendTeamCurrencies(String app, String idCurrency, String quantity, String idTeam)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/v2/teams/" + idTeam + "/currencies/spend?currencies[" + idCurrency + "]=" + quantity) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Crea un equipo.
     * @param app
     * @param name
     * @param idTeam
     * @return
     */
    RestResponse createTeam(String app, String name, String idTeam)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/v2/teams?name=" + name + "&tag=" + idTeam) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Permite restar una cantidad de un tipo de moneda a un jugador.
     * @param app
     * @param idCurrency
     * @param quantity
     * @param idPlayer
     * @return
     */
    RestResponse removeCurrenciesFromPlayer(String app, String idCurrency, String quantity, String idPlayer)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/currencies/remove?currencies[" + idCurrency + "]=-" + quantity + "&id_in_app=" + idPlayer) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Permite restar una cantidad de un tipo de moneda a un equipo.
     * @param app
     * @param idCurrency
     * @param quantity
     * @param idTeam
     * @return
     */
    RestResponse removeCurrenciesFromTeam(String app, String idCurrency, String quantity, String idTeam)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.post("http://playngage.io/api/v2/teams/" + idTeam + "/currencies?currencies[" + idCurrency + "]=-" + quantity) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    // ---------------------------------------------------------------------------------------------------------------------------
    // MÉTODOS PUT
    // ---------------------------------------------------------------------------------------------------------------------------

    /***
     * Permite a un jugador cambiar de equipo.
     * @param app
     * @param idTeam
     * @param idPlayer
     * @return
     */
    RestResponse changeTeam(String app, String idTeam, String idPlayer)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.put("http://playngage.io/api/v2/teams/" + idTeam + "/players/" + idPlayer + "?exclude=members,pending&options=move_contributions") { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    // ---------------------------------------------------------------------------------------------------------------------------
    // MÉTODOS DELETE
    // ---------------------------------------------------------------------------------------------------------------------------

    /***
     * Elimina un jugador.
     * @param app
     * @param idPlayer
     * @return
     */
    RestResponse deletePlayer(String app, String idPlayer)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.delete("http://playngage.io/api/players/" + idPlayer) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }

    /***
     * Elimina un equipo.
     * @param app
     * @param idTeam
     * @return
     */
    RestResponse deleteTeam(String app, String idTeam)
    {
        RestBuilder rest
        RestResponse resp
        rest = new RestBuilder()
        resp = rest.delete("http://playngage.io/api/teams/" + idTeam) { header 'Authorization', 'Token token=' + app header 'Accept', '*/*' }
        return resp
    }
}