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

    // Listado de estudiantes de todas las secciones del profesor.
    User[] estudiantesProf = []

    // Listado de facciones de todas las secciones del profesor.
    Faccion[] faccionesProf = []

	@Secured(['ROLE_STUDENT','ROLE_ADMIN'])
	def index()
    {
        try
        {
            User currentUser = springSecurityService.getCurrentUser()
            UserRole role = UserRole.find { user.id == currentUser.id && role.authority == "ROLE_STUDENT" }
            if (role != null) redirect action: 'dashboard'
            if(estudiantesProf.length == 0 || faccionesProf.length == 0) Seccion.findAll { profesor.username == springSecurityService.getCurrentUser().username }.each { appService.traerSeccion(it.nombre).facciones.each { fac -> estudiantesProf += fac.miembros; faccionesProf += fac } }
            [userName: currentUser.username]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
	}

	/**
	 * Carga el dashboard
	 **/
	@Secured(['ROLE_STUDENT'])
	def dashboard()
    {
        User user
        def semanas = []
        def estrellas = []
        def porcentajes = []
        Faccion[] facciones = []

        try
        {
            user = appService.traerDatosEstudiante(springSecurityService.getCurrentUser().nombre.toString())
            // Instancia datos del usuario y de su pestaña individual
            for (int i = 0; i < appService.numeroQuincenas; i++)
            {
                semanas.add(i + 1)
                estrellas.add(user.estrellasSemanas[i])
                porcentajes.add(user.aporteSemanas[i])
            }
            facciones = appService.traerSeccion(user.faccion.nombreFaccion.substring(0, 9)).facciones
            [userName: user.username, user: user, semanas: semanas, estrellas: estrellas, porcentajes: porcentajes, facciones: facciones]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
	}
	
	def dashboardEstudiante()
	{
		User user
		def semanas = []
		def estrellas = []
		def porcentajes = []
        Faccion[] facciones = []

        try
        {
            if (params['username'] != null) user = appService.traerDatosEstudiante(params['username'].toString())
            if (user != null)
            {
                // Instancia datos del usuario y de su pestaña individual
                for (int i = 0; i < appService.numeroQuincenas; i++)
                {
                    semanas.add(i + 1)
                    estrellas.add(user.estrellasSemanas[i])
                    porcentajes.add(user.aporteSemanas[i])
                }
                facciones = appService.traerSeccion(user.faccion.nombreFaccion.substring(0, 9)).facciones
            }
            else user = springSecurityService.getCurrentUser()
            [userName: springSecurityService.getCurrentUser().username, user: user, semanas: semanas, estrellas: estrellas, porcentajes: porcentajes, users: estudiantesProf, facciones: facciones]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
	}

	def comprarFaccion()
    {
        try
        {
            [userName: springSecurityService.getCurrentUser().username, facciones: faccionesProf.sort(false) { it.nombreFaccion }]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
	}
	
	def comprarFaccionSave()
    {
		String message
		Integer value

        try
        {
            value = params.int('value1')
            message = appService.gastarMonedasFaccion(params['faccionId'].toString(), value)
            message = 'La compra del poder ha sido exitosa. ' + message
            [userName: springSecurityService.getCurrentUser().username, message: message]
        }
        catch(ServicioException ex)
        {
            [userName: springSecurityService.getCurrentUser().username, message: 'No se compró el poder. ' + ex.message]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            faccionesProf = []
            Seccion.findAll { profesor.username == springSecurityService.getCurrentUser().username }.each { appService.traerSeccion(it.nombre).facciones.each { fac -> estudiantesProf += fac.miembros; faccionesProf += fac } }
        }
	}

	def comprarEnGrupo()
    {
        try
        {
            [userName: springSecurityService.getCurrentUser().username, users: estudiantesProf.sort(false) { it.nombre }]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
	}

	def comprarEnGrupoSave()
    {
		String message
		Integer value1
		Integer value2
        ArrayList<String> idEstudiantes
        ArrayList<Integer> cantidades

        try
        {
            value1 = params.int('value1')
            value2 = params.int('value2')
            idEstudiantes = new ArrayList<String>()
            cantidades = new ArrayList<Integer>()
            idEstudiantes.addAll([params['userId1'], params['userId2']] as String[])
            cantidades.addAll([value1, value2] as Integer[])
            message = appService.gastarGemasGrupo(idEstudiantes, cantidades)
            message = 'La compra de la ayuda ha sido exitosa. ' + message
            [userName: springSecurityService.getCurrentUser().username, message: message]
        }
        catch(ServicioException ex)
        {
            [userName: springSecurityService.getCurrentUser().username, message: 'No se compró la ayuda. ' + ex.message]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            faccionesProf = []
            Seccion.findAll { profesor.username == springSecurityService.getCurrentUser().username }.each { appService.traerSeccion(it.nombre).facciones.each { fac -> estudiantesProf += fac.miembros; faccionesProf += fac } }
        }
	}

	def comprarEjercicios()
	{
        try
        {
            [userName: springSecurityService.getCurrentUser().username, users: estudiantesProf.sort(false) { it.nombre }]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
	}

	def comprarEjerciciosSave()
    {
		String message
		Integer value

        try
        {
            value = params.int('value1')
            message = appService.gastarGemasEstudiante(params['userId1'].toString(), value)
            message = 'La compra del ejercicio ha sido exitosa. ' + message
            [userName: springSecurityService.getCurrentUser().username, message: message]
        }
        catch(ServicioException ex)
        {
            [userName: springSecurityService.getCurrentUser().username, message: 'No se compró el ejercicio. ' + ex.message]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            faccionesProf = []
            Seccion.findAll { profesor.username == springSecurityService.getCurrentUser().username }.each { appService.traerSeccion(it.nombre).facciones.each { fac -> estudiantesProf += fac.miembros; faccionesProf += fac } }
        }
	}

	private void upload(String csvFile)
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
	}
	
	private void proceseLinea(def linea)
    {
		String userId
		String prueba
		String tipoPrueba
		int numPrueba
		int quincena
		String scoreTxt = "0"
        String mensaje
        double score

        try
        {
            userId = linea[0]
            prueba = linea[6]
            tipoPrueba = prueba.subSequence(0,1)
            quincena = Integer.parseInt(prueba.subSequence(2,4))
            if (linea[7] != null) if (!linea[7].trim().equals("")) scoreTxt = linea[7]
            score = Double.parseDouble(scoreTxt.trim().replace("%", "").replace("\"", "").replace(",", "."))
            if (tipoPrueba == 'M')
            {
                mensaje = appService.registrarPrueba(appService.MECANICA + quincena.toString(), userId, score.toInteger())
                System.out.println("Usuario: " + userId + " - Mensaje: " + mensaje)
            }
            else if (tipoPrueba == 'C')
            {
                numPrueba = Integer.parseInt(prueba.substring(prueba.length() - 2))
                if (numPrueba == 1) mensaje = appService.registrarPrueba(appService.COGNITIVA_FACIL + quincena.toString(), userId, score.toInteger())
                else if (numPrueba == 2) mensaje = appService.registrarPrueba(appService.COGNITIVA_MEDIA + quincena.toString(), userId, score.toInteger())
                else if (numPrueba == 3) mensaje = appService.registrarPrueba(appService.COGNITIVA_DIFICIL + quincena.toString(), userId, score.toInteger())
                System.out.println("Usuario: " + userId + " - Mensaje: " + mensaje)
            }
            else if (tipoPrueba == 'H')
            {
                mensaje = appService.registrarPrueba(appService.HONORIFICA + quincena.toString(), userId, score.toInteger())
                System.out.println("Usuario: " + userId + " - Mensaje: " + mensaje)
            }
        }
        catch(ServicioException ex)
        {
            System.out.println("Usuario: " + userId + " - Error: " + ex.message)
        }
	}
	
	def solicitarArchivo()
    {
        try
        {
            String message = params['message']
            if (message == null) message = ""
            [userName: springSecurityService.getCurrentUser().username, message: message]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
	}

    def editarEstudiantes()
    {
        try
        {
            [userName: springSecurityService.getCurrentUser().username]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
    }
	
	def cargarInformacion()
    {
        try
        {
            MultipartFile archivo = request.getFile('archivo')
            def split
            if (archivo && (split = archivo.getOriginalFilename().split('\\.')).length > 1 && split[split.length - 1] == 'csv')
            {
                String nombreArchivo = grailsApplication.config.co.edu.uniandes.uploadfolder + archivo.getOriginalFilename()
                File archivoLocal = new File(nombreArchivo)
                archivo.transferTo(archivoLocal)
                upload(nombreArchivo)
            }
            else throw new ServicioException("Debe cargar un archivo en formato csv")
            redirect(action: 'index')
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            faccionesProf = []
            Seccion.findAll { profesor.username == springSecurityService.getCurrentUser().username }.each { appService.traerSeccion(it.nombre).facciones.each { fac -> estudiantesProf += fac.miembros; faccionesProf += fac } }
        }
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
            registros.each{ try {message = appService.eliminarEstudiante(it[0]); System.out.println(message)} catch(ServicioException ex){System.out.println(ex.message)} }
            registros.each{ try {message = appService.eliminarFaccion('Seccion ' + it[3] + ' Faccion ' + it[4]); System.out.println(message)} catch(ServicioException ex){System.out.println(ex.message)} }
            registros.each{ try {message = appService.crearFaccion('Seccion ' + it[3] + ' Faccion ' + it[4]); System.out.println(message)} catch(ServicioException ex){System.out.println(ex.message)} }
            registros.each{ try {message = appService.crearEstudiante(it[0], it[2] + ' ' + it[1], it[0] + '@uniandes.edu.co', 'Seccion ' + it[3] + ' Faccion ' + it[4]); System.out.println(message)} catch(ServicioException ex){System.out.println(ex.message)} }
            redirect(action: 'index')
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            faccionesProf = []
            Seccion.findAll { profesor.username == springSecurityService.getCurrentUser().username }.each { appService.traerSeccion(it.nombre).facciones.each { fac -> estudiantesProf += fac.miembros; faccionesProf += fac } }
        }
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