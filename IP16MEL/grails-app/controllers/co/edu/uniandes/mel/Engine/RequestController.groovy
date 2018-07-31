package co.edu.uniandes.mel.Engine

import co.edu.uniandes.login.Administrador
import co.edu.uniandes.login.Estudiante
import co.edu.uniandes.login.Seccion
import co.edu.uniandes.mel.excepciones.ServicioException
import org.springframework.web.multipart.MultipartFile

import co.edu.uniandes.login.Equipo
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

    // Listado de estudiantes de todas las secciones del profesor.
    Estudiante[] estudiantesProf = []

    // Listado de equipos de todas las secciones del profesor.
    Equipo[] equiposProf = []

    // Username del último usuario que ingresó a la aplicación.
    String lstUsuario

    /**
     * Despliega la vista principal del dashboard.
     */
	@Secured(['ROLE_STUDENT','ROLE_ADMIN','ROLE_SUPERADMIN'])
	def index()
    {
        try
        {
            User currentUser = springSecurityService.getCurrentUser()
            UserRole role = UserRole.find { user.id == currentUser.id && role.authority == "ROLE_STUDENT" }
            if (role != null) redirect action: 'dashboard'
            if(estudiantesProf.length == 0 || equiposProf.length == 0 || lstUsuario != springSecurityService.getCurrentUser().username)
            {
                lstUsuario = springSecurityService.getCurrentUser().username
                estudiantesProf = []
                equiposProf = []
                Administrador.findByUser(currentUser).secciones.each { appService.traerSeccion(it.nombre).equipos.each { eq -> estudiantesProf += eq.miembros; equiposProf += eq } }
            }
            [userName: currentUser.username]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
	}

	/**
	 * Despliega el dashboard de un estudiante.
	 **/
	@Secured(['ROLE_STUDENT'])
	def dashboard()
    {
        Estudiante estudiante
        def monedasTotal = []
        def semanas = []
        def monedas = []
        def porcentajes = []
        Equipo[] equipos = []

        try
        {
            estudiante = appService.traerDatosEstudiante(springSecurityService.getCurrentUser().username.toString())
            // Instancia datos del usuario y de su pestaña individual
            for (int i = 0; i < appService.NUM_SEMANAS; i++)
            {
                semanas.add(i + 1)
                monedas.add(estudiante.monedasSemanas[i])
                porcentajes.add(estudiante.aporteSemanas[i])
            }
            equipos = appService.traerSeccion(estudiante.equipo.nombre.substring(0, 17).trim()).equipos.sort{it.nombre.substring(it.nombre.lastIndexOf(' ')).toInteger()}
            for (int i = 0; i < equipos.length; i++)
            {
                monedasTotal.add(0)
                for (int k = 0; k < equipos[i].miembros.size(); k++)
                {
                    monedasTotal[monedasTotal.size() - 1] += equipos[i].miembros[k].monedas
                }
            }
            [userName: estudiante.user.username, estudiante: estudiante, semanas: semanas, monedas: monedas, monedasTotal: monedasTotal, porcentajes: porcentajes, equipos: equipos]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
	}

    /**
     * Despliega el dashboard de un estudiante para ser visualizado por un profesor.
     **/
	def dashboardEstudiante()
	{
		Estudiante estudiante
        def monedasTotal = []
		def semanas = []
		def monedas = []
		def porcentajes = []
        Equipo[] equipos = []

        try
        {
            if (params['username'] != null) estudiante = appService.traerDatosEstudiante(params['username'].toString())
            if (estudiante != null)
            {
                // Instancia datos del usuario y de su pestaña individual
                for (int i = 0; i < appService.NUM_SEMANAS; i++)
                {
                    semanas.add(i + 1)
                    monedas.add(estudiante.monedasSemanas[i])
                    porcentajes.add(estudiante.aporteSemanas[i])
                }
                equipos = appService.traerSeccion(estudiante.equipo.nombre.substring(0, 17).trim()).equipos.sort{it.nombre.substring(it.nombre.lastIndexOf(' ')).toInteger()}
                for (int i = 0; i < equipos.length; i++)
                {
                    monedasTotal.add(0)
                    for (int k = 0; k < equipos[i].miembros.size(); k++)
                    {
                        monedasTotal[monedasTotal.size() - 1] += equipos[i].miembros[k].monedas
                    }
                }
            }
            else estudiante = new Estudiante(user: new User())
            [userName: springSecurityService.getCurrentUser().username, estudiante: estudiante, semanas: semanas, monedas: monedas, monedasTotal: monedasTotal, porcentajes: porcentajes, estudiantes: estudiantesProf, equipos: equipos]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
	}

    /**
     * Despliega la vista para comprar un poder para un equipo.
     */
	def comprarEquipo()
    {
        try
        {
            [userName: springSecurityService.getCurrentUser().username, equipos: equiposProf.sort(false) { it.nombre }]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
	}

    /**
     * Despliega la vista de confirmación de compra de un poder por parte de un equipo.
     */
	def comprarEquipoSave()
    {
		String mensaje
		Integer value

        try
        {
            value = params.int('value1')
            mensaje = appService.gastarMonedasEquipo(params['equipoId'].toString(), value)
            mensaje = 'La compra del poder ha sido exitosa. ' + mensaje
            System.out.println(mensaje + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            [userName: springSecurityService.getCurrentUser().username, message: mensaje]
        }
        catch(ServicioException ex)
        {
            mensaje = 'No se compró el poder. ' + ex.message
            System.out.println(mensaje + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            [userName: springSecurityService.getCurrentUser().username, message: mensaje]
        }
        catch(Exception ex)
        {
            System.out.println(ex.message + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            render("<h3>Ha ocurrido un error</h3><p>" + ex.message + "</p>")
        }
        finally
        {
            estudiantesProf = []
            equiposProf = []
            Administrador.findByUser(springSecurityService.getCurrentUser() as User).secciones.each { appService.traerSeccion(it.nombre).equipos.each { eq -> estudiantesProf += eq.miembros; equiposProf += eq } }
        }
	}

    /**
     * Despliega la vista para comprar una ayuda para una parejas de estudiantes.
     */
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

    /**
     * Despliega la vista de confirmación de compra de una ayuda por parte de una pareja de estudiantes.
     */
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
            System.out.println(mensaje + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            [userName: springSecurityService.getCurrentUser().username, message: mensaje]
        }
        catch(ServicioException ex)
        {
            mensaje = 'No se compró la ayuda. ' + ex.message
            System.out.println(mensaje + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            [userName: springSecurityService.getCurrentUser().username, message: mensaje]
        }
        catch(Exception ex)
        {
            System.out.println(ex.message + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            equiposProf = []
            Administrador.findByUser(springSecurityService.getCurrentUser() as User).secciones.each { appService.traerSeccion(it.nombre).equipos.each { eq -> estudiantesProf += eq.miembros; equiposProf += eq } }
        }
	}

    /**
     * Despliega la vista para comprar un ejercicio para un estudiante.
     */
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

    /**
     * Despliega la vista de confirmación de compra de un ejercicio por parte de un estudiante.
     */
	def comprarEjerciciosSave()
    {
		String mensaje
		Integer value

        try
        {
            value = params.int('value1')
            mensaje = appService.gastarGemasEstudiante(params['userId1'].toString(), value)
            mensaje = 'La compra del ejercicio ha sido exitosa. ' + mensaje
            System.out.println(mensaje + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            [userName: springSecurityService.getCurrentUser().username, message: mensaje]
        }
        catch(ServicioException ex)
        {
            mensaje = 'No se compró el ejercicio. ' + ex.message
            System.out.println(mensaje + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            [userName: springSecurityService.getCurrentUser().username, message: mensaje]
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage() + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            equiposProf = []
            Administrador.findByUser(springSecurityService.getCurrentUser() as User).secciones.each { appService.traerSeccion(it.nombre).equipos.each { eq -> estudiantesProf += eq.miembros; equiposProf += eq } }
        }
	}

    /**
     * Procesa los datos de los archivos de notas de Sicua.
     * @param csvFile
     *        Archivo de notas en formato csv.
     */
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

    /**
     * Procesa una línea específica de un archivo de notas de Sicua.
     * @param linea
     *        Línea en formato csv.
     */
	private void proceseLinea(def linea)
    {
		String userId
		String prueba
		String tipoPrueba
		int numPrueba
		int semana
		String scoreTxt = "0"
        String mensaje
        double score

        try
        {
            prueba = linea[6]
            userId = linea[0]
            tipoPrueba = prueba.subSequence(0,1)
            semana = Integer.parseInt(prueba.subSequence(2,4))
            if (linea[7] != null) if (!linea[7].trim().equals("")) scoreTxt = linea[7]
            score = Double.parseDouble(scoreTxt.trim().replace("%", "").replace("\"", "").replace(",", "."))
            if (tipoPrueba == 'M')
            {
                mensaje = appService.registrarPrueba(appService.MECANICA + semana.toString(), userId, score.toInteger())
                System.out.println("Usuario: " + userId + " - Mensaje: " + mensaje + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            }
            else if (tipoPrueba == 'C')
            {
                numPrueba = Integer.parseInt(prueba.substring(prueba.length() - 2))
                if (numPrueba == 1) mensaje = appService.registrarPrueba(appService.COGNITIVA_FACIL + semana.toString(), userId, score.toInteger())
                else if (numPrueba == 2) mensaje = appService.registrarPrueba(appService.COGNITIVA_MEDIA + semana.toString(), userId, score.toInteger())
                else if (numPrueba == 3) mensaje = appService.registrarPrueba(appService.COGNITIVA_DIFICIL + semana.toString(), userId, score.toInteger())
                System.out.println("Usuario: " + userId + " - Mensaje: " + mensaje + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            }
            else if (tipoPrueba == 'H')
            {
                mensaje = appService.registrarPrueba(appService.HONORIFICA + semana.toString(), userId, score.toInteger())
                System.out.println("Usuario: " + userId + " - Mensaje: " + mensaje + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            }
        }
        catch(ServicioException ex)
        {
            System.out.println("Usuario: " + userId + " - Error: " + ex.message + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
        }
	}

    /**
     * Despliega la vista para seleccionar el archivo de notas de Sicua a cargar.
     */
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

    /**
     * Despliega la vista para editar usuarios.
     */
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

    /**
     * Despliega la vista para cargar un archivo de notas de Sicua.
     */
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
            System.out.println(ex.getMessage() + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            equiposProf = []
            Administrador.findByUser(springSecurityService.getCurrentUser() as User).secciones.each { appService.traerSeccion(it.nombre).equipos.each { eq -> estudiantesProf += eq.miembros; equiposProf += eq } }
        }
	}

    /**
     * Método que se invoca para cargar automáticamente un archivo de notas de Sicua.
     */
    @Secured(['permitAll'])
    def cargarAutomat()
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

                // Imprimir el estado actual del juego.
                System.out.println("--- Inicio estado del juego MEL --- " + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
                Administrador.findByNombre('se-busto').secciones.sort{it.nombre}.each{ appService.traerSeccion(it.nombre).equipos.each { eq ->
                    System.out.println("Equipo: " + eq.nombre + " pts: " + eq.puntos + " mon: " + eq.monedas)
                    eq.miembros.each {mie -> System.out.println("Miembro: " + mie.nombre + " gem: " + mie.gemas + " med: " + mie.medallas + " pts: " +mie.puntos + " mon: " + mie.monedas) }
                }}
                System.out.println("--- Fin estado del juego MEL ---")
            }
            else throw new ServicioException("Debe cargar un archivo en formato csv")
            redirect(action: 'index')
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage() + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
    }

    /**
     * Permite cargar estudiantes a partir de un archivo csv.
     */
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
            registros.each{ try {message = appService.eliminarEstudiante(it[0]); System.out.println(message  + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))} catch(ServicioException ex){System.out.println(ex.message + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))} }
            registros.each{ try {message = appService.eliminarEquipo('Seccion ' + it[1] + ' Equipo ' + it[2]); System.out.println(message + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))} catch(ServicioException ex){System.out.println(ex.message + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))} }
            registros.each{ try {message = appService.crearEquipo('Seccion ' + it[1] + ' Equipo ' + it[2]); System.out.println(message + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))} catch(ServicioException ex){System.out.println(ex.message + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))} }
            registros.each{
                try
                {
                    message = appService.crearEstudiante(it[0], it[0], it[0] + '@uniandes.edu.co', 'Seccion ' + it[1] + ' Equipo ' + it[2]); System.out.println(message + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
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
                    System.out.println(ex.message + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
                }
            }
            redirect(action: 'index')
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage() + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            equiposProf = []
            Administrador.findByUser(springSecurityService.getCurrentUser() as User).secciones.each { appService.traerSeccion(it.nombre).equipos.each { eq -> estudiantesProf += eq.miembros; equiposProf += eq } }
        }
    }

    /**
     * Permite cargar profesores a partir de un archivo csv.
     */
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
                    if(seccion == null) seccion = new Seccion(nombre: 'Seccion ' + sec, equipos: [], estudiantes: [])
                    profesor.addToSecciones(seccion)
                }
                profesor.save(flush: true)
                UserRole.create profesor.user, Role.get(1), true
            }
            redirect(action: 'index')
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage() + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            equiposProf = []
            Administrador.findByUser(springSecurityService.getCurrentUser() as User).secciones.each { appService.traerSeccion(it.nombre).equipos.each { eq -> estudiantesProf += eq.miembros; equiposProf += eq } }
        }
    }

    /**
     * Despliega la vista para reiniciar las monedas de todos los equipos de una sección.
     */
    @Secured(['ROLE_SUPERADMIN', 'ROLE_ADMIN'])
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

    /**
     * Despliega la vista de confirmación de reinicio de monedas de una sección.
     */
    @Secured(['ROLE_SUPERADMIN', 'ROLE_ADMIN'])
    def reiniciarMonedasSave()
    {
        String mensaje

        try
        {
            mensaje = appService.reiniciarMonedasSeccion(params['nombreSeccion'].toString())
            mensaje = 'El reinicio de monedas ha sido exitoso. ' + mensaje
            System.out.println(mensaje + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            [userName: springSecurityService.getCurrentUser().username, message: mensaje]
        }
        catch(Exception ex)
        {
            System.out.println(ex.message + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            equiposProf = []
            Administrador.findByUser(springSecurityService.getCurrentUser() as User).secciones.each { appService.traerSeccion(it.nombre).equipos.each { eq -> estudiantesProf += eq.miembros; equiposProf += eq } }
        }
    }

    /**
     * Permite a un jugador cambiar de equipo.
     */
    @Secured(['ROLE_SUPERADMIN', 'ROLE_ADMIN'])
    def cambiarEquipos()
    {
        int cnt
        int veces = 0
        int resJugadores = 0
        int numJugadores
        String equipoAct
        String nombreEquipo
        String usuEstudiante
        String mensaje

        try
        {
            numJugadores = Integer.parseInt((String) params.numMiembros)
            equipoAct = params.equipoAc
            cnt = Integer.parseInt((String) params.cnt)
            for(int i = 0; i < numJugadores; i++)
            {
                usuEstudiante = params['miembro_' + i].toString()
                nombreEquipo = params['selEquipo_' + i].toString()
                if(nombreEquipo == 'nuevo')
                {
                    nombreEquipo = equipoAct.substring(0, equipoAct.length() - 1) + cnt
                    if(veces == 0)
                    {
                        mensaje = appService.crearEquipo(nombreEquipo)
                        System.out.println(mensaje + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
                    }
                    veces++
                    mensaje = appService.cambiarEquipo(nombreEquipo, usuEstudiante)
                    System.out.println(mensaje + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
                }
                else if(nombreEquipo != 'igual')
                {
                    mensaje = appService.cambiarEquipo(nombreEquipo, usuEstudiante)
                    System.out.println(mensaje + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
                }
                else resJugadores++
            }
            if(resJugadores == 0)
            {
                mensaje = appService.eliminarEquipo(equipoAct)
                System.out.println(mensaje + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            }
            redirect(action: 'dashboardEstudiante', params: [username: usuEstudiante])
        }
        catch(Exception ex)
        {
            System.out.println(ex.message + ' - MEL:' + springSecurityService.currentUser?.username + ' ' + new Date().format( 'yyyy-MM-dd HH:mm:ss' ))
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
        finally
        {
            estudiantesProf = []
            equiposProf = []
            Administrador.findByUser(springSecurityService.getCurrentUser() as User).secciones.each { appService.traerSeccion(it.nombre).equipos.each { eq -> estudiantesProf += eq.miembros; equiposProf += eq } }
        }
    }
}