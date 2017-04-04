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

	// Configuracion puntajes,gemas,monedas
	public static final double MINSCORECOG = 80.0
	public static final double MINSCOREHON = 80.0
	
	public static final int PTOSCOGFACIL = 5
	public static final int PTOSCOGMEDIO = 12
	public static final int PTOSCOGDIFICIL = 20
	
	public static final int GEMCOGFACIL = 1
	public static final int GEMCOGMEDIO = 2
	public static final int GEMCOGDIFICIL = 3
	
	public static final int HONORMEDALLAS = 1
	public static final int HONORPUNTOS = 30
	
	public static final int TOTALPRUEBASMEC = 300

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
		faccion1.miembros = faccion1.miembros.sort(false){-it.puntos}
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
		faccion2.miembros = faccion2.miembros.sort(false){-it.puntos}
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
	
	def comprarFaccion() {
		def facciones = Faccion.findAll{}.sort(false){it.nombreFaccion}
		[facciones: facciones]
	}
	
	def comprarFaccionSave() {
		String message = "Compra exitosa"
		Faccion faccion = Faccion.find{id == params.int('faccionId')}
		Integer value = params.int('value1')
		//System.out.println(params.int('faccionId') + " " + value)
		if(value>faccion.monedas) {
			message = faccion.nombreFaccion + " no tiene suficientes monedas"
		} else {
			faccion.monedas -= value
			faccion.save(flush: true)
		}
		[message: message]
	}

	def comprarEnGrupo() {
		def users = User.findAll{}.sort(false){it.username}
		[users: users]
	}

	def comprarEnGrupoSave() {
		String message = "Compra exitosa"
		User user1 = User.find{id == params.int('userId1')}
		User user2 = User.find{id == params.int('userId2')}
		Integer value1 = params.int('value1')
		Integer value2 = params.int('value2')
		System.out.println(value1 + " " + value2 + " " + params['userId1'])
		if(value1>user1.gemas) {
			message = user1.username + " no tiene suficientes gemas"
		} else if(value2>user2.gemas) {
			message = user2.username + " no tiene suficientes gemas"
		} else {
			user1.gemas -= value1
			user2.gemas -= value2
			user1.save(flush: true)
			user2.save(flush: true)
		}

		[message: message]
	}

	def comprarEjercicios() {
		def users = User.findAll{}.sort(false){it.username}
		[users: users]
	}

	def comprarEjerciciosSave() {
		String message = "Compra exitosa"
		User user = User.find{id == params.int('userId1')}
		Integer value = params.int('value1')
		if(value>user.gemas) {
			message = user.username + " no tiene suficientes gemas"
		} else {
			user.gemas -= value
			user.save(flush: true)
		}

		[message: message]
	}

	def upload() {
		String csvFile = "c:\\tmp\\" + params["archivo"];
		int semana = params.int('semana')
		System.out.println(csvFile + semana)
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
		
	
		try {
			def mecanicasUsuarios= [:]
			
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] linea = line.split(cvsSplitBy);
				System.out.println(line + " -- " + cvsSplitBy)
				if(!linea[6].contains("Total")) {
					proceseLinea(linea,semana,mecanicasUsuarios);
				}
			}
			proceseTotalesMecanicos(mecanicasUsuarios, semana-1)
	
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
		redirect(uri: "/")
	}

	
	def proceseLinea(String[] linea, int semana,def mecanicasUsuarios) {
		Double totalUser=0.0
		String userId = linea[0]
		String prueba = linea[6]
		String  tipoPrueba = prueba.subSequence(0,1)
		int numPrueba = Integer.parseInt(prueba.substring(prueba.length() - 2))
		int sem = Integer.parseInt(prueba.subSequence(2,4))
		String scoreTxt = "0"
		if(linea[7]!=null) {
			if(!linea[7].trim().equals("")) {
				scoreTxt = linea[7]
			}
		}
		//System.out.println("Prueba:" + prueba + " sub: " + prueba.substring(prueba.length() - 2) + " num: " + prueba.subSequence(2,4) + "scoreTxt: " + scoreTxt)
		double score = Double.parseDouble(scoreTxt.trim().replace("%", "").replace("\"", ""));
		
		User user = User.find{username == userId}
		Faccion faccion = user.faccion
		
		if ((tipoPrueba == 'M' ) && (semana==sem) ) {
			System.out.println("Tipo prueba: " + tipoPrueba)
			totalUser = mecanicasUsuarios[userId]
			if  (totalUser == null) {
				mecanicasUsuarios[userId] = score
			} else {
				mecanicasUsuarios[userId] =score + totalUser
			}
		} else if ((tipoPrueba == 'C') && (score >= MINSCORECOG) && (sem==semana-1)) {
			System.out.println("Tipo prueba: " + tipoPrueba + " - " + numPrueba)
		
				if (numPrueba==1) {
					// agregar al usuario userId PTOSCOGFACIL puntos  GEMCOGFACIL gemas
					// agragar a la faccion del usuario PTOSCOGFACIL puntos
					user.puntos += PTOSCOGFACIL
					user.gemas += GEMCOGFACIL
					faccion.puntos += PTOSCOGFACIL
					
				} else if (numPrueba==2) {
					// agregar al usuario userId PTOSCOGMEDIO puntos  GEMCOGMEDIO gemas
					// agragar a la faccion del usuario PTOSCOGMEDIO puntos
					user.puntos += PTOSCOGMEDIO
					user.gemas += GEMCOGMEDIO
					faccion.puntos += PTOSCOGMEDIO
				
				} else if (numPrueba==3) {
				
					// agregar al usuario userId PTOSCOGDIFICIL puntos  GEMCOGDIFICIL gemas
					// agragar a la faccion del usuario PTOSCOGDIFICIL puntos
					user.puntos += PTOSCOGDIFICIL
					user.gemas += GEMCOGDIFICIL
					faccion.puntos += PTOSCOGDIFICIL

				}
				user.save(flush: true)
				faccion.save(flush: true)
		} else if ((tipoPrueba == 'H') && (score > MINSCOREHON)) {
			// Agragar al usuario userId HONORPUNTOS y HONORMEDALLAS
			// Agregar a la faccion de usuario HONORPUNTOS
			user.puntos += HONORPUNTOS
			user.medallas += HONORMEDALLAS
			faccion.puntos += HONORPUNTOS
			user.save(flush: true)
			faccion.save(flush: true)
		}
	}
	
	def proceseTotalesMecanicos(def mecanicasUsuarios, int semana)	{

		def keys = mecanicasUsuarios.keySet();
		
		
		
		mecanicasUsuarios.each { userId, total ->
			User user = User.find{username == userId}
			Faccion faccion = user.faccion
			//System.out.println("Usuario: " + user.username + " faccion " + faccion.nombreFaccion + " Total " + total)
			if (total == 300) {
				System.out.println("Total " + total)
				// Agragar al usuario userId 5 estrella en la semana
				// Agragar al usuario userId 1 gema
				// Agregar a la facción 10 monedas
				user.estrellasSemanas[semana] = 5
				user.gemas++
				faccion.monedas += 10
				
			} else if (total >= 270) {
				System.out.println("Total " + total)
 				// Agragar al usuario userId 4 estrella
				// Agregar a la facción 5 monedas
				user.estrellasSemanas[semana] = 4
				faccion.monedas += 5
			} else if (total >= 240) {
				System.out.println("Total " + total)
				// Agragar al usuario userId 3 estrella
				// Agregar a la facción 4 monedas
				user.estrellasSemanas[semana] = 3
				faccion.monedas += 4
			} else if (total >= 180) {
				System.out.println("Total " + total)
				// Agragar al usuario userId 2 estrella
				// Agregar a la facción 3 monedas
				user.estrellasSemanas[semana] = 2
				faccion.monedas += 3
			} else if (total >= 105) {
				System.out.println("Total " + total)
				// Agragar al usuario userId 1 estrella
				// Agregar a la facción 2 monedas
				user.estrellasSemanas[semana] = 1
				faccion.monedas += 2
			}
			user.save(flush: true)
			faccion.save(flush: true)
		}
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
