package co.edu.uniandes.mel.Engine

import co.edu.uniandes.mel.excepciones.ServicioException
import org.springframework.web.multipart.MultipartFile

import co.edu.uniandes.login.Faccion
import co.edu.uniandes.login.Role
import co.edu.uniandes.login.User
import co.edu.uniandes.login.UserRole
import co.edu.uniandes.login.Seccion
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
@Transactional
class RequestController
{

    // Permite utilizar las constantes del config
	def grailsApplication

    // Permite acceder a la información del usuario de la sesión
	def springSecurityService

    // Servicio de aplicación MEL gamificada.
    def appService

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
		for(int i=0; i<appService.numeroQuincenas; i++) {
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
	
	def dashboardEstudiante()
	{
		User user
		User[] users = []
		def semanas = []
		def estrellas = []
		def porcentajes = []
        Faccion[] facciones = []

        Seccion.findAll{profesor.username == springSecurityService.getCurrentUser().username}.each{ appService.traerSeccion(it.nombre).facciones.each{ fac -> users += fac.miembros } }
        if(params['username'] != null) user = appService.traerDatosEstudiante(params['username'].toString())
        if(user!=null)
        {
			// Instancia datos del usuario y de su pestaña individual
			for(int i=0; i < appService.numeroQuincenas; i++)
            {
				semanas.add(i+1)
				estrellas.add(user.estrellasSemanas[i])
				porcentajes.add(user.aporteSemanas[i])
			}
            facciones = appService.traerSeccion(user.faccion.nombreFaccion.substring(0, 9)).facciones
		}
        else user = springSecurityService.getCurrentUser()

        [user: user, semanas: semanas, estrellas: estrellas, porcentajes: porcentajes, users: users, facciones: facciones]
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

	def upload(String csvFile)
    {
		BufferedReader br = null
		String line
		String cvsSplitBy = "\\|"
        String[] linea
	
		try
        {
			br = new BufferedReader(new FileReader(csvFile))
			while ((line = br.readLine()) != null)
            {
				line = line.replaceAll("\"", "")
				linea = line.split(cvsSplitBy)
				if(linea[6].startsWith("MQ") || linea[6].startsWith("HQ") || linea[6].startsWith("CQ")) proceseLinea(linea)
			}
		}
        catch (FileNotFoundException e)
        {
            e.printStackTrace()
        }
        catch (IOException e)
        {
            e.printStackTrace()
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close()
                }
                catch (IOException e)
                {
                    e.printStackTrace()
                }
            }
        }
		redirect(uri: "/")
	}
	
	def proceseLinea(def linea)
    {
		String userId = linea[0]
		String prueba = linea[6]
		String tipoPrueba = prueba.subSequence(0,1)
		int numPrueba
		int quincena = Integer.parseInt(prueba.subSequence(2,4))
		String scoreTxt = "0"
        String mensaje
        double score

		if(linea[7] != null) if(!linea[7].trim().equals("")) scoreTxt = linea[7]
        score = Double.parseDouble(scoreTxt.trim().replace("%", "").replace("\"", "").replace(",", "."));

		if(tipoPrueba == 'M')
        {
            try
            {
                mensaje = appService.registrarPrueba(appService.MECANICA + quincena.toString(), userId.replace('.', '-'), score.toInteger())
                System.out.println("Usuario: " + userId + " - Mensaje: " + mensaje)
            }
            catch(Exception ex)
            {
                System.out.println("Usuario: " + userId + " - Error: " + ex.message)
            }
		}
        else if(tipoPrueba == 'C')
        {
            try
            {
                numPrueba = Integer.parseInt(prueba.substring(prueba.length() - 2))
                if (numPrueba == 1) mensaje = appService.registrarPrueba(appService.COGNITIVA_FACIL + quincena.toString(), userId.replace('.', '-'), score.toInteger())
                else if (numPrueba == 2) mensaje = appService.registrarPrueba(appService.COGNITIVA_MEDIA + quincena.toString(), userId.replace('.', '-'), score.toInteger())
                else if (numPrueba == 3) mensaje = appService.registrarPrueba(appService.COGNITIVA_DIFICIL + quincena.toString(), userId.replace('.', '-'), score.toInteger())
                System.out.println("Usuario: " + userId + " - Mensaje: " + mensaje)
            }
            catch(Exception ex)
            {
                System.out.println("Usuario: " + userId + " - Error: " + ex.message)
            }
		}
        else if(tipoPrueba == 'H')
        {
            try
            {
                mensaje = appService.registrarPrueba(appService.HONORIFICA + quincena.toString(), userId.replace('.', '-'), score.toInteger())
                System.out.println("Usuario: " + userId + " - Mensaje: " + mensaje)
            }
            catch(Exception ex)
            {
                System.out.println("Usuario: " + userId + " - Error: " + ex.message)
            }
		}
	}
	
	def solicitarArchivo() {
		String message = params['message']
		if(message==null) {
			message=""
		}
		[message: message]
	}

    def editarEstudiantes()
    {

    }
	
	def cargarInformacion()
    {
		MultipartFile archivo = request.getFile('archivo')
		String message = "Datos cargados correctamente"
		def split
		if(archivo&&(split = archivo.getOriginalFilename().split('\\.')).length>1&&split[split.length-1]=='csv')
        {
			String nombreArchivo = grailsApplication.config.co.edu.uniandes.uploadfolder + archivo.getOriginalFilename()
			File archivoLocal = new File(nombreArchivo)
			archivo.transferTo(archivoLocal)
			upload(nombreArchivo)
		}
        else message = "Debe cargar un archivo en formato csv"

		render message
	}

    def cargarEstudiantes()
    {
        String nombreArchivo
        File archivoLocal
        BufferedReader br
        String line
        String cvsSplitBy = ";"
        MultipartFile archivo = request.getFile('archivo')
        String message
        String[] linea
        ArrayList<String[]> registros = new ArrayList<String[]>()

        try
        {
            nombreArchivo = grailsApplication.config.co.edu.uniandes.uploadfolder + archivo.getOriginalFilename()
            archivoLocal = new File(nombreArchivo)
            archivo.transferTo(archivoLocal)

            br = new BufferedReader(new FileReader(nombreArchivo))
            br.readLine()
            while((line = br.readLine()) != null)
            {
                linea = line.split(cvsSplitBy)
                registros.add(linea)
            }
            br.close()
            registros.each{ try {message = appService.eliminarEstudiante(it[0].replace('.', '-')); System.out.println(message)} catch(ServicioException ex){System.out.println(ex.message)} }
            registros.each{ try {message = appService.eliminarFaccion('Seccion ' + it[3] + ' Faccion ' + it[4]); System.out.println(message)} catch(ServicioException ex){System.out.println(ex.message)} }
            registros.each{ try {message = appService.crearFaccion('Seccion ' + it[3] + ' Faccion ' + it[4]); System.out.println(message)} catch(ServicioException ex){System.out.println(ex.message)} }
            registros.each{ try {message = appService.crearEstudiante(it[0].replace('.', '-'), it[2] + ' ' + it[1], it[0] + '@uniandes.edu.co', 'Seccion ' + it[3] + ' Faccion ' + it[4]); System.out.println(message)} catch(ServicioException ex){System.out.println(ex.message)} }
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }

        redirect(uri: "/")
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
