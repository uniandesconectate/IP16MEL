import co.edu.uniandes.login.Administrador
import co.edu.uniandes.login.Role
import co.edu.uniandes.login.User
import co.edu.uniandes.login.UserRole

class BootStrap {
	def GrailsApplication
	
    def init = { servletContext ->
        inicializarBD()
    }

    def destroy = {}
	
	def inicializarBD()
    {
        Administrador profesor
		def roles = []
        def usuarios = []

        roles.add(new Role(authority: GrailsApplication.config.co.edu.uniandes.login.role.admin))
        roles.add(new Role(authority: GrailsApplication.config.co.edu.uniandes.login.role.student))
        roles.add(new Role(authority: GrailsApplication.config.co.edu.uniandes.login.role.superadmin))
        roles.each { rolValue -> rolValue.save(flush: true) }

        usuarios.add(new User(username: "cr.calle", password:'ks3d7fcd8$f1'))
        usuarios.add(new User(username: "gcortes", password:'ks3d7fcd8$f1'))
        usuarios.add(new User(username: "fj.moreno", password:'ks3d7fcd8$f1'))
        usuarios.add(new User(username: "se-busto", password:'ks3d7fcd8$f1'))
        usuarios.each { usuValue ->
            usuValue.save(flush: true)
            profesor = new Administrador()
            profesor.nombre = usuValue.username
            profesor.user = usuValue
            profesor.secciones = []
            profesor.save(flush: true)
            UserRole.create profesor.user, ((Role) roles.get(2)), true
        }
	}
}