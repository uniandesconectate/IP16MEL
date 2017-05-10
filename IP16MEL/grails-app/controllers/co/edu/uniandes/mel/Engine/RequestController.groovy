package co.edu.uniandes.mel.Engine

import java.util.ArrayList;
import org.springframework.web.multipart.MultipartFile

import co.edu.uniandes.login.Faccion
import co.edu.uniandes.login.Role
import co.edu.uniandes.login.User
import co.edu.uniandes.login.UserRole
import co.edu.uniandes.login.Seccion
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
	
	public static final int PTOSCOGFACIL = 3
	public static final int PTOSCOGMEDIO = 8
	public static final int PTOSCOGDIFICIL = 20
	
	public static final int GEMCOGFACIL = 1
	public static final int GEMCOGMEDIO = 2
	public static final int GEMCOGDIFICIL = 3
	
	public static final int HONORMEDALLAS = 1
	public static final int HONORPUNTOS = 30
	
	public static final int TOTALPRUEBASMEC = 300
	
	public static final int SEMANAINICIAL = 13

	def grailsApplication //Permite utilizar las constantes del config
	def springSecurityService //Permite acceder a la informaci�n del usuario de la sesi�n
	int numeroSemanas = 2
	
	String token = "210fd18fe01d086fe1f6ed60f789137b" //Token de API en PlayNGage
	String url = "http://playngage.io/api/" //Token de API en PlayNGage
	String idInApp = 'rca1' //Este es el id_in_app de un jugador <-- Puede ser el username Uniandes o el ID de una plataforma
	String action_tag = 'intercept_test' //Este es el tag de una acci�n
	String testGroup = "First"

	@Secured(['ROLE_STUDENT','ROLE_ADMIN'])
	def index() {
		User currentUser = springSecurityService.getCurrentUser()
		UserRole role = UserRole.find{user.id == currentUser.id && role.authority == "ROLE_STUDENT"}
		//System.out.println(currentUser.username + "-" + role)
		if(role!=null) {
			redirect action: 'dashboard'
		}
	}
	/**
	 * Carga el dashboard
	 **/
	@Secured(['ROLE_STUDENT'])
	def dashboard() {
		User user = springSecurityService.getCurrentUser()
		
		// Instancia datos del usuario y de su pesta�a individual
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
		
		Faccion faccion1 = user.faccion
		faccion1.miembros = faccion1.miembros.sort(false){-it.puntos}
		// Instancia datos de la primera facci�n
		String faccion1Copas = faccion1.puntos
		String faccion1Monedas = faccion1.monedas
		String faccion1NombreFaccion = faccion1.nombreFaccion
		def faccion1Nombres = []
		def faccion1Medallas = []
		def faccion1Puntos = []

		faccion1.miembros.each { miembro ->
			faccion1Nombres.add(miembro.username)
			faccion1Medallas.add(miembro.medallas)
			faccion1Puntos.add(miembro.puntos)
		}
		int faccion1PromedioPuntos =0
		int faccion1PromedioMonedas=0
		int totalMiembros1 = faccion1.miembros.size()
		if (totalMiembros1 != 0) {
			faccion1PromedioPuntos=faccion1.puntos/totalMiembros1
			faccion1PromedioMonedas=faccion1.monedas/totalMiembros1
		}

		System.out.println(" Faccion 1  Puntos:"+faccion1PromedioPuntos + " Monedas:"+ faccion1PromedioMonedas)
		// Instancia datos de la segunda facci�n
		Faccion faccion2 = Faccion.find{id != faccion1.id && seccion.id == faccion1.seccion.id}
		faccion2.miembros = faccion2.miembros.sort(false){-it.puntos}
		String faccion2NombreFaccion = faccion2.nombreFaccion
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
		int totalMiembros2 = faccion2.miembros.size()
		int faccion2PromedioPuntos =0
		int faccion2PromedioMonedas =0

		if (totalMiembros2 != 0) {
			faccion2PromedioPuntos=faccion2.puntos/totalMiembros2
			faccion2PromedioMonedas=faccion2.monedas/totalMiembros2
		}
		System.out.println(" Faccion 2  Puntos:"+faccion2PromedioPuntos + " Monedas:"+ faccion2PromedioMonedas)
		
		[userName: userName, user: user, semanas: semanas, estrellas: estrellas, porcentajes: porcentajes, gemas: gemas, medallas: medallas,
			faccion1Nombres: faccion1Nombres, faccion1Medallas: faccion1Medallas, faccion1Puntos: faccion1Puntos,
			faccion1Copas: faccion1Copas, faccion1Monedas: faccion1Monedas, faccion1NombreFaccion: faccion1NombreFaccion,faccion1PromedioPuntos: faccion1PromedioPuntos,faccion1PromedioMonedas: faccion1PromedioMonedas,
			faccion2Nombres: faccion2Nombres, faccion2Medallas: faccion2Medallas, faccion2Puntos: faccion2Puntos,
			faccion2Copas: faccion2Copas, faccion2Monedas: faccion2Monedas, faccion2NombreFaccion: faccion2NombreFaccion, faccion2PromedioPuntos: faccion2PromedioPuntos,faccion2PromedioMonedas: faccion2PromedioMonedas]
	}
	
	def dashboardEstudiante() {
		//Ejemplo de llamado a un m�todo para retornar el usuario
		User user = User.find{username==params['username']}
		//System.out.println(params['username'])
		def users = [] // User.findAll()
		String userName = ""
		def semanas = []
		def estrellas = []
		def porcentajes = []
		String gemas = ""
		String medallas = ""

		String faccion1Copas = ""
		String faccion1Monedas = ""
		def faccion1Nombres = []
		def faccion1Medallas = []
		def faccion1Puntos = []

		def faccion2Nombres = []
		def faccion2Medallas = []
		def faccion2Puntos = []
		String faccion2Copas = ""
		String faccion2Monedas = ""
		String faccion1NombreFaccion
		String faccion2NombreFaccion
		int faccion1PromedioPuntos =0
		int faccion1PromedioMonedas =0
		int faccion2PromedioPuntos =0
		int faccion2PromedioMonedas =0
		
		User userProfesor = springSecurityService.getCurrentUser()
		def secciones = Seccion.findAll{profesor.username == userProfesor.username}
		//System.out.println(secciones)
		secciones.each { seccion ->
			users = users + seccion.estudiantes
		}
		users = users.sort(false){it.nombre}

		if(user!=null) {			
			// Instancia datos del usuario y de su pestaña individual
			userName = user.username
			for(int i=0;i<numeroSemanas;i++) {
				semanas.add(i+1)
				estrellas.add(user.estrellasSemanas[i])
				porcentajes.add(user.aporteSemanas[i])
			}
			gemas = user.gemas
			medallas = user.medallas
			
			Faccion faccion1 = user.faccion
			faccion1.miembros = faccion1.miembros.sort(false){-it.puntos}
			// Instancia datos de la primera facci�n
			faccion1Copas = faccion1.puntos
			faccion1Monedas = faccion1.monedas
			faccion1NombreFaccion = faccion1.nombreFaccion
			faccion1.miembros.each { miembro ->
				faccion1Nombres.add(miembro.username)
				faccion1Medallas.add(miembro.medallas)
				faccion1Puntos.add(miembro.puntos)
			}
	
			// Instancia datos de la segunda facci�n
			Faccion faccion2 = Faccion.find{id != faccion1.id && seccion.id == faccion1.seccion.id}
			faccion2.miembros = faccion2.miembros.sort(false){-it.puntos}
			faccion2Copas = faccion2.puntos
			faccion2Monedas = faccion2.monedas
			faccion2NombreFaccion = faccion2.nombreFaccion
			faccion2.miembros.each { miembro ->
				faccion2Nombres.add(miembro.username)
				faccion2Medallas.add(miembro.medallas)
				faccion2Puntos.add(miembro.puntos)
			}
			int totalMiembros1 = faccion1.miembros.size()
			
			if (totalMiembros1 != 0) {
						faccion1PromedioPuntos=faccion1.puntos/totalMiembros1
						faccion1PromedioMonedas=faccion1.monedas/totalMiembros1
			}
			System.out.println(" Faccion 1  Puntos:"+faccion1PromedioPuntos + " Monedas:"+ faccion1PromedioMonedas)
			int totalMiembros2 = faccion2.miembros.size()
	
			if (totalMiembros2 != 0) {
				faccion2PromedioPuntos=faccion2.puntos/totalMiembros2
				faccion2PromedioMonedas=faccion2.monedas/totalMiembros2
			}
			System.out.println(" Faccion 2  Puntos:"+faccion2PromedioPuntos + " Monedas:"+ faccion2PromedioMonedas)
	
		} else {
			user = springSecurityService.getCurrentUser()
		}

		

		[userName: userName, user: user, semanas: semanas, estrellas: estrellas, porcentajes: porcentajes, gemas: gemas, medallas: medallas,
			faccion1Nombres: faccion1Nombres, faccion1Medallas: faccion1Medallas, faccion1Puntos: faccion1Puntos,
			faccion1Copas: faccion1Copas, faccion1Monedas: faccion1Monedas,  faccion1NombreFaccion: faccion1NombreFaccion,,faccion1PromedioPuntos: faccion1PromedioPuntos,faccion1PromedioMonedas: faccion1PromedioMonedas,
			faccion2Nombres: faccion2Nombres, faccion2Medallas: faccion2Medallas, faccion2Puntos: faccion2Puntos,
			faccion2Copas: faccion2Copas, faccion2Monedas: faccion2Monedas,  faccion2NombreFaccion: faccion2NombreFaccion,faccion2PromedioPuntos: faccion2PromedioPuntos,faccion2PromedioMonedas: faccion2PromedioMonedas, users: users]
	}

	def comprarFaccion() {
		User userProfesor = springSecurityService.getCurrentUser()
		def secciones = Seccion.findAll{profesor.username == userProfesor.username}
		def facciones = []
		secciones.each { seccion ->
			facciones = facciones + seccion.facciones
		}
		facciones = facciones.sort(false){it.nombreFaccion}
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
		def users = []
		User userProfesor = springSecurityService.getCurrentUser()
		def secciones = Seccion.findAll{profesor.username == userProfesor.username}
		//System.out.println(secciones)
		secciones.each { seccion ->
			users = users + seccion.estudiantes
		}
		users = users.sort(false){it.nombre}
		[users: users]
	}

	def comprarEnGrupoSave() {
		String message = "Compra exitosa"
		User user1 = User.find{id == params.int('userId1')}
		User user2 = User.find{id == params.int('userId2')}
		Integer value1 = params.int('value1')
		Integer value2 = params.int('value2')
		//System.out.println(value1 + " " + value2 + " " + params['userId1'])
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
		def users = []
		User userProfesor = springSecurityService.getCurrentUser()
		def secciones = Seccion.findAll{profesor.username == userProfesor.username}
		//System.out.println(secciones)
		secciones.each { seccion ->
			users = users + seccion.estudiantes
		}
		users = users.sort(false){it.nombre}
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

	def upload(String csvFile, int semana, String separador) {
//		String csvFile = "c:\\tmp\\" + params["archivo"];
//		int semana = params.int('semana')
		//System.out.println(csvFile + semana)
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = "\\|";
		
	
		try {
			def mecanicasUsuarios= [:]
			
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				// use comma as separator
				line = line.replaceAll("\"", "")
				//System.out.println(line + " -- " + cvsSplitBy)
				String[] linea = line.split(cvsSplitBy);
				//System.out.println("Linea[6]" + linea[6].startsWith("MS"))
				if(linea[6].startsWith("MS") || linea[6].startsWith("HS") || linea[6].startsWith("CS")) {
					proceseLinea(linea,semana,mecanicasUsuarios);
				}
			}
			proceseTotalesMecanicos(mecanicasUsuarios, semana - SEMANAINICIAL)
	
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

	
	def proceseLinea(def linea, int semana,def mecanicasUsuarios) {
		Double totalUser=0.0
		String userId = linea[0]
		String prueba = linea[6]
		String  tipoPrueba = prueba.subSequence(0,1)
		//System.out.println("prueba" + prueba)
		int numPrueba = Integer.parseInt(prueba.substring(prueba.length() - 2))
		int sem = Integer.parseInt(prueba.subSequence(2,4))
		String scoreTxt = "0"
		if(linea[7]!=null) {
			if(!linea[7].trim().equals("")) {
				scoreTxt = linea[7]
			}
		}
		//System.out.println("Prueba:" + prueba + " sub: " + prueba.substring(prueba.length() - 2) + " num: " + prueba.subSequence(2,4) + " scoreTxt: " + scoreTxt.trim().replace("%", "").replace("\"", "").replace(",", "."))
		double score = Double.parseDouble(scoreTxt.trim().replace("%", "").replace("\"", "").replace(",", "."));
		
		User user = User.find{username == userId}
		System.out.println("Usuario: " + userId + "  - " + user)
		Faccion faccion = user.faccion
		//System.out.println(sem + " "  + semana)
		if ((tipoPrueba == 'M' ) && (semana==sem) ) {
			totalUser = mecanicasUsuarios[userId]
			if  (totalUser == null) {
				mecanicasUsuarios[userId] = score
			} else {
				mecanicasUsuarios[userId] =score + totalUser
			}
		} else if ((tipoPrueba == 'C') && (score >= MINSCORECOG) && (sem==semana-1)) {
			//System.out.println("Tipo prueba: " + tipoPrueba + " - " + numPrueba)
		
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
		//System.out.println("Tipo prueba: " + tipoPrueba)
	}
	
	def proceseTotalesMecanicos(def mecanicasUsuarios, int semana)	{

		def keys = mecanicasUsuarios.keySet();
		def totalMonedasSemanaFacciones= [:]
		int monedasFaccion=0;
		Integer totalMonedasSemanaFaccion;
		
		// Calcula y agraga modenas para cada usuario y calcula el total obetnido por cada faccion
		
		mecanicasUsuarios.each { userId, total ->
			User user = User.find{username == userId}
			Faccion faccion = user.faccion
			
			monedasFaccion = 0
			
			//System.out.println("Usuario: " + user.username + " faccion " + faccion.nombreFaccion + " Total " + total)
			//System.out.println("Usuario: " + user.username + "Total " + total)
			if (total == 300) {
				// Agragar al usuario userId 5 estrella en la semana
				// Agragar al usuario userId 1 gema
				// Agregar a la facci�n 5 monedas
				user.estrellasSemanas[semana] = 5
				user.gemas++
				monedasFaccion = 5
				
			} else if (total >= 270) {
 				// Agragar al usuario userId 4 estrella
				// Agregar a la facci�n 4 monedas
				user.estrellasSemanas[semana] = 4
				monedasFaccion =  4
			} else if (total >= 240) {
				// Agragar al usuario userId 3 estrella
				// Agregar a la facci�n 3 monedas
				user.estrellasSemanas[semana] = 3
				monedasFaccion =  3
			} else if (total >= 180) {
				// Agragar al usuario userId 2 estrella
				// Agregar a la facci�n 2 monedas
				user.estrellasSemanas[semana] = 2
				monedasFaccion =  2
			} else if (total >= 105) {
				// Agragar al usuario userId 1 estrella
				// Agregar a la facci�n 1 monedas
				user.estrellasSemanas[semana] = 1
				monedasFaccion =  1
			}
			faccion.monedas += monedasFaccion;
			
			totalMonedasSemanaFaccion = totalMonedasSemanaFacciones[faccion.nombreFaccion]
			if  (totalMonedasSemanaFaccion == null) {
				totalMonedasSemanaFacciones[faccion.nombreFaccion] = monedasFaccion
			} else {
				totalMonedasSemanaFacciones[faccion.nombreFaccion] = monedasFaccion + totalMonedasSemanaFaccion
			}
			
			user.save(flush: true)
			faccion.save(flush: true)
		}
		
		mecanicasUsuarios.each { userId, total ->
			User user = User.find{username == userId}
			Faccion faccion = user.faccion
			totalMonedasSemanaFaccion = totalMonedasSemanaFacciones[faccion.nombreFaccion]
			if  (totalMonedasSemanaFaccion != null && totalMonedasSemanaFaccion>0) {
				user.aporteSemanas[semana] = (double)(100*(user.estrellasSemanas[semana]/totalMonedasSemanaFaccion))
			}
			
			user.save(flush: true)
		}
		
		
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
	
	def solicitarArchivo() {
		String message = params['message']
		if(message==null) {
			message=""
		}
		[message: message]
	}
	
	def cargarInformacion() {
		MultipartFile archivo = request.getFile('archivo')
		int semana = params.int('semana')
		String separador = params["separado"]
		String message = "Datos cargados correctamente"
		def split
		if(archivo&&(split = archivo.getOriginalFilename().split('\\.')).length>1&&split[split.length-1]=='csv') {
			String nombreArchivo = grailsApplication.config.co.edu.uniandes.uploadfolder + archivo.getOriginalFilename()
			File archivoLocal = new File(nombreArchivo)
			archivo.transferTo(archivoLocal)
//			FileInputStream fis = new FileInputStream(archivoLocal)
//			archivo.transferTo(archivoLocal)
			upload(nombreArchivo, semana, separador)
		} else {
			message = "Debe cargar un archivo en formato csv"
		}
		render message
		//redirect(action: 'solicitarArchivo', params: [message: message])
	}
	
	@Secured(['ROLE_STUDENT', 'ROLE_ADMIN'])
	def llenarDatosEjemplo() {
		def usuarios = User.findAll()
		Random random = new Random()
		usuarios.each { usuario ->
			usuario.medallas = random.nextInt(4)+1
			usuario.gemas = random.nextInt(9)+1
			usuario.puntos = random.nextInt(99)+1
			for(int i=0;i<3;i++) {
				usuario.estrellasSemanas[i] = random.nextInt(4)+1 
				usuario.aporteSemanas[i] = random.nextInt(70) + 30
			}
			usuario.faccion.puntos += usuario.puntos
			usuario.faccion.monedas += random.nextInt(50)
		}
		redirect uri: "/"
	}
	
	@Secured(['ROLE_STUDENT', 'ROLE_ADMIN'])
	//http://localhost:8080/IP16MEL/request/llenarDatosPrueabMel
	def llenarDatosPrueabMel() {
		User user
		def ret=[]
		def facciones = Faccion.findAll{}.sort(false){it.id}
		def roles = Role.findAll{}.sort(false){it.id}
		//usuario.faccion = Faccion.findAll{}.sort(false){it.id}[0]
      
      
		redirect uri: "/"
	}

}
