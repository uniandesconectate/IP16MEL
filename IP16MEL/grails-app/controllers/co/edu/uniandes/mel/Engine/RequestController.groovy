package co.edu.uniandes.mel.Engine

import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.SpringSecurityService

@Secured(['permitAll'])
@Transactional
class RequestController {

	//static scaffold = true //Habilita el CRUD autom�tico si el controlador es de un dominio
	def grailsApplication //Permite utilizar las constantes del config
	def springSecurityService //Permite acceder a la informaci�n del usuario de la sesi�n
	
	String token = "210fd18fe01d086fe1f6ed60f789137b" //Token de API en PlayNGage
	String idInApp = 'rca1' //Este es el id_in_app de un jugador <-- Puede ser el username Uniandes o el ID de una plataforma
	String action_tag = 'intercept_test' //Este es el tag de una acci�n
	String testGroup = "First"
	
	/**
	 * Index que carga el dashboard
	 **/
	def index() {
		//Ejemplo de llamado a un m�todo para retornar el usuario
		def userData = getUserData("rca1_dev")
		
		// Instancia datos del usuario y de su pesta�a individual
		String userName = userData[0]
		String user = userData[1]
		def semanas = [2,3,4,5,6]
		def estrellas = [3,2,4,6,4]
		def porcentajes = [40,30,40,70,90]
		String gemas = "3"
		String medallas = "9"
		
		// Instancia datos de la primera facci�n
		String faccion1Copas = "220"
		String faccion1Monedas = "50"
		def faccion1Nombres = ["Carolina Castro", "Juan Orozco", "David Medina", "Adriana Jaramillo", "Diego Zuluaga"]
		def faccion1Medallas = [5,5,4,3,2]
		def faccion1Puntos = [90,89,85,88,80]

		// Instancia datos de la segunda facci�n
		String faccion2Copas = "230"
		String faccion2Monedas = "60"
		def faccion2Nombres = ["Ana Grajales", "Sandra Restrepo", "Lukas Giraldo", "Santiago Duque", "Felipe Arango"]
		def faccion2Medallas = [5,5,5,4,4]
		def faccion2Puntos = [90, 90, 89,89,85]
		
		[userName: userName, user: user, semanas: semanas, estrellas: estrellas, porcentajes: porcentajes, gemas: gemas, medallas: medallas,
			faccion1Nombres: faccion1Nombres, faccion1Medallas: faccion1Medallas, faccion1Puntos: faccion1Puntos, 
			faccion1Copas: faccion1Copas, faccion1Monedas: faccion1Monedas,
			faccion2Nombres: faccion2Nombres, faccion2Medallas: faccion2Medallas, faccion2Puntos: faccion2Puntos, 
			faccion2Copas: faccion2Copas, faccion2Monedas: faccion2Monedas]
	}

	def getUserData(String userName) {
		def ret = [] //Arreglo con los datos a retornar del motor a la aplicaci�n
		RestBuilder rest = new RestBuilder()
		RestResponse resp = rest.post("http://playngage.io/api/players" +
				"?id_in_app=" + userName) {
			header 'Authorization', 'Token token=' + token
			header 'Accept', '*/*'
		}
		
		ret.add(resp.json.player.id_in_app)
		ret.add(resp.json.player.email)
		//System.out.println(resp.getBody())
		return(ret)
	}

	//Completar una acci�n para un jugador
	def createPlayer() {
		String mode = "_dev" //grailsApplication.config.co.edu.uniandes.mel.mode //Modo del usuario, _dev en desarrollo y _prod en producci�n
		RestBuilder rest = new RestBuilder()
		RestResponse resp = rest.post("http://playngage.io/api/players" + 
				"?id_in_app=" + idInApp + "" + mode.toString() + 
				"&email=" + idInApp + "@uniandes.edu.co" +
				"&set_currencies=0,0,0,0" +
				"&team[tag]=" +  testGroup) {
			header 'Authorization', 'Token token=' + token
			header 'Accept', '*/*'
		}
		
		def playerStatus = resp.json.status
		def playerId = resp.json.player.id
		def playerIdInApp = resp.json.player.id_in_app 
		def playerEmail = resp.json.player.email 
		[playerStatus: playerStatus, playerId: playerId, playerIdInApp: playerIdInApp, playerEmail: playerEmail]
	}

	def getPlayerData() {
		String mode = "_dev" //grailsApplication.config.co.edu.uniandes.mel.mode //Modo del usuario, _dev en desarrollo y _prod en producci�n
		RestBuilder rest = new RestBuilder()
		RestResponse resp = rest.post("http://playngage.io/api/players" + 
				"?id_in_app=" + idInApp + "" + mode.toString()) {
			header 'Authorization', 'Token token=' + token
			header 'Accept', '*/*'
		}
		
//		def playerStatus = resp.json.status
//		def playerId = resp.json.player.id
//		def playerIdInApp = resp.json.player.id_in_app 
//		def playerEmail = resp.json.player.email 
//		[playerStatus: playerStatus, playerId: playerId, playerIdInApp: playerIdInApp, playerEmail: playerEmail]
		render resp.getBody()
	}
	
	def createFactions() {
		String name = "Faccion"
		def number = [1,2,3,4,5,6,7,8]
		def group = ["A","B"]
		RestBuilder rest = new RestBuilder()
		
		number.each { num->
			group.each { grp->
				String nameOfFaction = name + num + grp
				RestResponse resp = rest.post("http://playngage.io//api/teams" +
					"?name=" + nameOfFaction) {
				header 'Authorization', 'Token token=' + token
				header 'Accept', '*/*'
				}
			}
		}
		render "ok"
	}
}
