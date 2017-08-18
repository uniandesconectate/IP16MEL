package co.edu.uniandes.mel.Engine

import co.edu.uniandes.login.Administrador
import co.edu.uniandes.login.Estudiante
import co.edu.uniandes.login.Seccion
import co.edu.uniandes.mel.excepciones.ServicioException
import org.springframework.web.multipart.MultipartFile

import co.edu.uniandes.login.Faccion
import co.edu.uniandes.login.Role
import co.edu.uniandes.login.User
import co.edu.uniandes.login.UserRole
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_SUPERADMIN'])
class RequestController
{

    // Permite utilizar las constantes del config
	def grailsApplication

    // Permite acceder a la información del usuario de la sesión
	def springSecurityService

    // Servicio de aplicación MEL gamificada.
    def appService

    // Listado de estudiantes de todas las secciones del profesores.
    Estudiante[] estudiantesProf = []

    // Listado de facciones de todas las secciones del profesores.
    Faccion[] faccionesProf = []

    // Username del último usuario que ingresó a la aplicación.
    String lstUsuario

	@Secured(['ROLE_STUDENT','ROLE_ADMIN','ROLE_SUPERADMIN'])
	def index()
    {
        try
        {
            User currentUser = springSecurityService.getCurrentUser()
            UserRole role = UserRole.find { user.id == currentUser.id && role.authority == "ROLE_STUDENT" }
            if (role != null) redirect action: 'dashboard'
            if(estudiantesProf.length == 0 || faccionesProf.length == 0 || lstUsuario != springSecurityService.getCurrentUser().username)
            {
                lstUsuario = springSecurityService.getCurrentUser().username
                estudiantesProf = []
                faccionesProf = []
                Administrador.findByUser(currentUser).secciones.each { appService.traerSeccion(it.nombre).facciones.each { fac -> estudiantesProf += fac.miembros; faccionesProf += fac } }
            }
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
        Estudiante estudiante
        def quincenas = []
        def estrellas = []
        def porcentajes = []
        Faccion[] facciones = []

        try
        {
            estudiante = appService.traerDatosEstudiante(springSecurityService.getCurrentUser().username.toString())
            // Instancia datos del usuario y de su pestaña individual
            for (int i = 0; i < appService.NUM_QUINCENAS; i++)
            {
                quincenas.add(i + 1)
                estrellas.add(estudiante.estrellasQuincenas[i])
                porcentajes.add(estudiante.aporteQuincenas[i])
            }
            facciones = appService.traerSeccion(estudiante.faccion.nombreFaccion.substring(0, 9)).facciones
            [userName: estudiante.user.username, estudiante: estudiante, quincenas: quincenas, estrellas: estrellas, porcentajes: porcentajes, facciones: facciones]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
	}
	
	def dashboardEstudiante()
	{
		Estudiante estudiante
		def quincenas = []
		def estrellas = []
		def porcentajes = []
        Faccion[] facciones = []

        try
        {
            if (params['username'] != null) estudiante = appService.traerDatosEstudiante(params['username'].toString())
            if (estudiante != null)
            {
                // Instancia datos del usuario y de su pestaña individual
                for (int i = 0; i < appService.NUM_QUINCENAS; i++)
                {
                    quincenas.add(i + 1)
                    estrellas.add(estudiante.estrellasQuincenas[i])
                    porcentajes.add(estudiante.aporteQuincenas[i])
                }
                facciones = appService.traerSeccion(estudiante.faccion.nombreFaccion.substring(0, 9)).facciones
            }
            else estudiante = new Estudiante(user: new User())
            [userName: springSecurityService.getCurrentUser().username, estudiante: estudiante, quincenas: quincenas, estrellas: estrellas, porcentajes: porcentajes, estudiantes: estudiantesProf, facciones: facciones]
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
		String mensaje
		Integer value

        try
        {
            value = params.int('value1')
            mensaje = appService.gastarMonedasFaccion(params['faccionId'].toString(), value)
            mensaje = 'La compra del poder ha sido exitosa. ' + mensaje
            System.out.println(mensaje)
            [userName: springSecurityService.getCurrentUser().username, message: mensaje]
        }
        catch(ServicioException ex)
        {
            mensaje = 'No se compró el poder. ' + ex.message
            System.out.println(mensaje)
            [userName: springSecurityService.getCurrentUser().username, message: mensaje]
        }
        catch(Exception ex)
        {
            System.out.println(ex.message)
            render("<h3>Ha ocurrido un error</h3><p>" + ex.message + "</p>")
        }
        finally
        {
            estudiantesProf = []
            faccionesProf = []
            Administrador.findByUser(springSecurityService.getCurrentUser() as User).secciones.each { appService.traerSeccion(it.nombre).facciones.each { fac -> estudiantesProf += fac.miembros; faccionesProf += fac } }
        }
	}

	def comprarEnGrupo()
    {
        try
        {
            [userName: springSecurityService.getCurrentUser().username, estudiantes: estudiantesProf.sort(false) { it.nombre }]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
	}

	def comprarEnGrupoSave()
    {
		String mensaje
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
            mensaje = appService.gastarGemasGrupo(idEstudiantes, cantidades)
            mensaje = 'La compra de la ayuda ha sido exitosa. ' + mensaje
            System.out.println(mensaje)
            [userName: springSecurityService.getCurrentUser().username, message: mensaje]
        }
        catch(ServicioException ex)
        {
            mensaje = 'No se compró la ayuda. ' + ex.message
            System.out.println(mensaje)
            [userName: springSecurityService.getCurrentUser().username, message: mensaje]
        }
        catch(Exception ex)
        {
            System.out.println(ex.message)
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            faccionesProf = []
            Administrador.findByUser(springSecurityService.getCurrentUser() as User).secciones.each { appService.traerSeccion(it.nombre).facciones.each { fac -> estudiantesProf += fac.miembros; faccionesProf += fac } }
        }
	}

	def comprarEjercicios()
	{
        try
        {
            [userName: springSecurityService.getCurrentUser().username, estudiantes: estudiantesProf.sort(false) { it.nombre }]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
	}

	def comprarEjerciciosSave()
    {
		String mensaje
		Integer value

        try
        {
            value = params.int('value1')
            mensaje = appService.gastarGemasEstudiante(params['userId1'].toString(), value)
            mensaje = 'La compra del ejercicio ha sido exitosa. ' + mensaje
            System.out.println(mensaje)
            [userName: springSecurityService.getCurrentUser().username, message: mensaje]
        }
        catch(ServicioException ex)
        {
            mensaje = 'No se compró el ejercicio. ' + ex.message
            System.out.println(mensaje)
            [userName: springSecurityService.getCurrentUser().username, message: mensaje]
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage())
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            faccionesProf = []
            Administrador.findByUser(springSecurityService.getCurrentUser() as User).secciones.each { appService.traerSeccion(it.nombre).facciones.each { fac -> estudiantesProf += fac.miembros; faccionesProf += fac } }
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
				if(linea[6] in appService.TAGS_SICUA) proceseLinea(linea)
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
            prueba = linea[6]
            userId = linea[0]
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

    @Secured(['ROLE_SUPERADMIN'])
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

    @Secured(['ROLE_SUPERADMIN'])
    def editarUsuarios()
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

    @Secured(['ROLE_SUPERADMIN'])
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
            System.out.println(ex.getMessage())
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            faccionesProf = []
            Administrador.findByUser(springSecurityService.getCurrentUser() as User).secciones.each { appService.traerSeccion(it.nombre).facciones.each { fac -> estudiantesProf += fac.miembros; faccionesProf += fac } }
        }
	}

    @Secured(['ROLE_SUPERADMIN'])
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
        User user
        Estudiante estudiante

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
            registros.each{ try {message = appService.eliminarFaccion('Seccion ' + it[1] + ' Faccion ' + it[2]); System.out.println(message)} catch(ServicioException ex){System.out.println(ex.message)} }
            registros.each{ try {message = appService.crearFaccion('Seccion ' + it[1] + ' Faccion ' + it[2]); System.out.println(message)} catch(ServicioException ex){System.out.println(ex.message)} }
            registros.each{
                try
                {
                    message = appService.crearEstudiante(it[0], it[0], it[0] + '@uniandes.edu.co', 'Seccion ' + it[1] + ' Faccion ' + it[2]); System.out.println(message)
                    user = User.findByUsername(it[0])
                    if(user == null)
                    {
                        user = new User(username: it[0], password: 'L4m3nt0B0l')
                        user.save(flush: true)
                    }
                    estudiante = Estudiante.findByUser(user)
                    if(estudiante != null) estudiante.delete()
                    estudiante = new Estudiante()
                    estudiante.nombre = it[0]
                    estudiante.user = user
                    estudiante.save(flush: true)
                    UserRole.create estudiante.user, Role.get(2), true
                }
                catch(ServicioException ex)
                {
                    System.out.println(ex.message)
                }
            }
            redirect(action: 'index')
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage())
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            faccionesProf = []
            Administrador.findByUser(springSecurityService.getCurrentUser() as User).secciones.each { appService.traerSeccion(it.nombre).facciones.each { fac -> estudiantesProf += fac.miembros; faccionesProf += fac } }
        }
    }

    @Secured(['ROLE_SUPERADMIN'])
    def cargarProfesores()
    {
        String nombreArchivo
        File archivoLocal
        BufferedReader br
        String line
        String cvsSplitBy = ";"
        MultipartFile archivo = request.getFile('archivo')
        String[] linea
        ArrayList<String[]> registros = new ArrayList<String[]>()
        User user
        Administrador profesor
        Seccion seccion

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
            registros.each{
                user = User.findByUsername(it[0])
                if(user == null)
                {
                    user = new User(username: it[0], password: 'ks3d7fcd8$f1')
                    user.save(flush: true)
                }
                profesor = Administrador.findByUser(user)
                if(profesor != null) profesor.delete()
                profesor = new Administrador()
                profesor.nombre = it[0]
                profesor.user = user
                profesor.secciones = []
                it[1].split('&').each {sec ->
                    seccion = Seccion.findByNombre('Seccion ' + sec)
                    if(seccion == null) seccion = new Seccion(nombre: 'Seccion ' + sec, facciones: [], estudiantes: [])
                    profesor.addToSecciones(seccion)
                }
                profesor.save(flush: true)
                UserRole.create profesor.user, Role.get(1), true
            }
            redirect(action: 'index')
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage())
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            faccionesProf = []
            Administrador.findByUser(springSecurityService.getCurrentUser() as User).secciones.each { appService.traerSeccion(it.nombre).facciones.each { fac -> estudiantesProf += fac.miembros; faccionesProf += fac } }
        }
    }

    @Secured(['ROLE_SUPERADMIN'])
    def reiniciarMonedas()
    {
        try
        {
            [userName: springSecurityService.getCurrentUser().username, secciones: Administrador.findByUser(springSecurityService.getCurrentUser() as User).secciones]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
    }

    @Secured(['ROLE_SUPERADMIN'])
    def reiniciarMonedasSave()
    {
        String mensaje

        try
        {
            mensaje = appService.reiniciarMonedasSeccion(params['nombreSeccion'].toString())
            mensaje = 'El reinicio de monedas ha sido exitoso. ' + mensaje
            System.out.println(mensaje)
            [userName: springSecurityService.getCurrentUser().username, message: mensaje]
        }
        catch(Exception ex)
        {
            System.out.println(ex.message)
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            faccionesProf = []
            Administrador.findByUser(springSecurityService.getCurrentUser() as User).secciones.each { appService.traerSeccion(it.nombre).facciones.each { fac -> estudiantesProf += fac.miembros; faccionesProf += fac } }
        }
    }
}