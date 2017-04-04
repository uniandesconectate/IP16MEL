package co.edu.uniandes.mel.Engine

import java.util.ArrayList;

import co.edu.uniandes.login.Faccion
import co.edu.uniandes.login.User
import co.edu.uniandes.mel.dto.FaccionDTO
import co.edu.uniandes.mel.dto.UsuarioDTO;
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.SpringSecurityService

@Secured(['ROLE_ADMIN'])
@Transactional
class RequestController {
	public static final int GET = 1
	public static final int POST = 2
	//static scaffold = true //Habilita el CRUD automático si el controlador es de un dominio
	def grailsApplication //Permite utilizar las constantes del config
	def springSecurityService //Permite acceder a la información del usuario de la sesión
	int numeroSemanas = 3
	
	String token = "210fd18fe01d086fe1f6ed60f789137b" //Token de API en PlayNGage
	String url = "http://playngage.io/api/" //Token de API en PlayNGage
	String idInApp = 'rca1' //Este es el id_in_app de un jugador <-- Puede ser el username Uniandes o el ID de una plataforma
	String action_tag = 'intercept_test' //Este es el tag de una acción
	String testGroup = "First"

	/**
	 * Index que carga el dashboard
	 **/
	@Secured(['ROLE_STUDENT'])
	def index() {
		//Ejemplo de llamado a un método para retornar el usuario
		User user = springSecurityService.getCurrentUser()
		
		// Instancia datos del usuario y de su pestaña individual
		String userName = user.username
		def semanas = []
		def estrellas = []
		def porcentajes = []
		for(int i=0;i<numeroSemanas;i++) {
			semanas.add(i+1)
			estrellas.add(user.estrellasSemanas[i])
			porcentajes.add(user.aporteSemanas[i])
		}
		String gemas = user.gemas
		String medallas = user.medallas
		
		Faccion faccion1 = Faccion.find{id == 1}
		faccion1.miembros = faccion1.miembros.sort(false){-it.medallas}
		// Instancia datos de la primera facción
		String faccion1Copas = faccion1.puntos
		String faccion1Monedas = faccion1.monedas
		def faccion1Nombres = []
		def faccion1Medallas = []
		def faccion1Puntos = []
		faccion1.miembros.each { miembro ->
			faccion1Nombres.add(miembro.username)
			faccion1Medallas.add(miembro.medallas)
			faccion1Puntos.add(miembro.puntos)
		}

		// Instancia datos de la segunda facción
		Faccion faccion2 = Faccion.find{id == 2}
		faccion2.miembros = faccion2.miembros.sort(false){-it.medallas}
		def faccion2Nombres = []
		def faccion2Medallas = []
		def faccion2Puntos = []
		String faccion2Copas = faccion2.puntos
		String faccion2Monedas = faccion2.monedas
		faccion2.miembros.each { miembro ->
			faccion2Nombres.add(miembro.username)
			faccion2Medallas.add(miembro.medallas)
			faccion2Puntos.add(miembro.puntos)
		}
		
		[userName: userName, user: user, semanas: semanas, estrellas: estrellas, porcentajes: porcentajes, gemas: gemas, medallas: medallas,
			faccion1Nombres: faccion1Nombres, faccion1Medallas: faccion1Medallas, faccion1Puntos: faccion1Puntos,
			faccion1Copas: faccion1Copas, faccion1Monedas: faccion1Monedas,
			faccion2Nombres: faccion2Nombres, faccion2Medallas: faccion2Medallas, faccion2Puntos: faccion2Puntos,
			faccion2Copas: faccion2Copas, faccion2Monedas: faccion2Monedas]
	}
	
	def comprar1() {
		def users = User.findAll.sort(false){it.username}
		[users: users]
	}
	
	def comprar1Save() {
		User user1 = User.find{username == params['username1']}
		User user2 = User.find{username == params['username2']}
		Integer value1 = params.int('value1')
		Integer value2 = params.int('value2')
		redirect(url: '/')
	}

	def comprar2() {
		def users = User.findAll.sort(false){it.username}
		[users: users]
	}

	def comprar2Save() {
		User user1 = User.find{username == params['username1']}
		User user2 = User.find{username == params['username2']}
		Integer value1 = params.int('value1')
		Integer value2 = params.int('value2')
		redirect(url: '/')
	}

	def comprar3() {
		def users = User.findAll.sort(false){it.username}
		[users: users]
	}

	def comprar3Save() {
		User user1 = User.find{username == params['username1']}
		User user2 = User.find{username == params['username2']}
		Integer value1 = params.int('value1')
		Integer value2 = params.int('value2')
		redirect(url: '/')
	}


	/**
	 * Llamado general a un servicio de playNGage
	 * @param apiName el nombre del api a llamar
	 * @param parameters map de parámetros a enviar
	 * @return restResponse la respuesta del llamado al servicio
	 */
	RestResponse callRequest(String apiName, def parameters, int method) {
		RestResponse restResoponse = null
		String concatenateSymbol = "?"
		if(apiName!=null) {
			if(!apiName.trim().equals("")) {
				String urlCall = url + apiName
				String[] paramKeys = parameters.keySet();
				if(paramKeys!=null) {
					for(int i=0;i<paramKeys.length;i++) {
						urlCall += concatenateSymbol + paramKeys[i] + "=" + parameters[paramKeys[i]]
						concatenateSymbol = "&"
					}
				}
				System.out.println(urlCall)
				RestBuilder restBuilder = new RestBuilder()
				if(method==GET) {
					restResoponse = restBuilder.get(urlCall) {
						header 'Authorization', 'Token token=' + token
						header 'Accept', '*/*'
					}
				} else {
					restResoponse = restBuilder.post(urlCall) {
						header 'Authorization', 'Token token=' + token
						header 'Accept', '*/*'
					}
				}
			}
		}
		return(restResoponse)
	}
	
	FaccionDTO getTeam(String tag) {
		FaccionDTO faccion = null
		RestResponse restResponse= callRequest("v2/teams/" + tag, [:], GET)
		String successMessage = restResponse.json.success
		if(successMessage!="false") {
			faccion = new FaccionDTO()
			faccion.setNombreFaccion(restResponse.json.name)
			faccion.setMonedas(restResponse.json.currencies.team_currencies.monedas.quantity)
			if(restResponse.json.members!=null) {
				ArrayList<UsuarioDTO> usuarios = new ArrayList<UsuarioDTO>();
				restResponse.json.members.each { member ->
					UsuarioDTO usuario = new UsuarioDTO();
					usuario.setNombreUsuario(member)
					usuarios.add(usuario)
				}
			}
			//System.out.println(restResponse.getBody())
			System.out.println(faccion.getNombreFaccion())
		}
		return(faccion)
	}
	
	def upload() {
		String csvFile = "c:\\tmp\\prueba.txt";
	    BufferedReader br = null;
	    String line = "";
	    String cvsSplitBy = ",";
	
	    try {
	
			br = new BufferedReader(new FileReader(csvFile));
	        while ((line = br.readLine()) != null) {
				// use comma as separator
	            String[] country = line.split(cvsSplitBy);
	
	            System.out.println("Country [code= " + country[1] + " , name=" + country[2] + "]");
            }
	
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
		render "listo"
	}
	
	/**
	 * Index que carga el dashboard anterior
	 **/
	def index1() {
		//Ejemplo de llamado a un método para retornar el usuario
		def userData = [:]
		userData["id_in_app"] = "rca1_dev" 
		RestResponse restResponse= callRequest("players", userData, GET)
		
		// Instancia datos del usuario y de su pestaña individual
		String userName = restResponse.json.player.id_in_app
		String user = restResponse.json.player.email
		def semanas = [2,3,4,5,6]
		def estrellas = [3,2,4,6,4]
		def porcentajes = [40,30,40,70,90]
		String gemas = "3"
		String medallas = "9"
		
		FaccionDTO faccion = getTeam("faccion1a")
		// Instancia datos de la primera facción
		String faccion1Copas = "220"
		String faccion1Monedas = "50"
		def faccion1Nombres = ["Carolina Castro", "Juan Orozco", "David Medina", "Adriana Jaramillo", "Diego Zuluaga"]
		def faccion1Medallas = [2,2,2,2,2]
		def faccion1Puntos = [90,89,85,88,80]

		// Instancia datos de la segunda facción
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
		def ret = [] //Arreglo con los datos a retornar del motor a la aplicación
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

	//Completar una acción para un jugador
	def createPlayer() {
		String mode = "_dev" //grailsApplication.config.co.edu.uniandes.mel.mode //Modo del usuario, _dev en desarrollo y _prod en producción
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
		String mode = "_dev" //grailsApplication.config.co.edu.uniandes.mel.mode //Modo del usuario, _dev en desarrollo y _prod en producción
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
					"?name=" + nameOfFaction + "&tag=" + nameOfFaction.toLowerCase()) {
				header 'Authorization', 'Token token=' + token
				header 'Accept', '*/*'
				}
			}
		}
		render "ok"
	}
}
