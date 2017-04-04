import co.edu.uniandes.login.Faccion
import co.edu.uniandes.login.Role
import co.edu.uniandes.login.User;
import co.edu.uniandes.login.UserRole;

class BootStrap {
	def GrailsApplication
	
    def init = { servletContext ->
		def roles=createRoles()
		def users=createUsers(roles)
    }
    def destroy = {
    }
	
	def createRoles() {
		def ret=[]
		ret.add(new Role(authority: GrailsApplication.config.co.edu.uniandes.login.role.admin))
		ret.add(new Role(authority: GrailsApplication.config.co.edu.uniandes.login.role.student))
		
		ret.each { retValue ->
			retValue.save(flush: true)
		}
		
		return(ret)
	}

	def createUsers(def roles) {
		
		Faccion faccion1 = new Faccion(nombreFaccion: "Faccion 1", miembros: [])
		faccion1.save(flush: true)

		Faccion faccion2 = new Faccion(nombreFaccion: "Faccion 2", miembros: [])
		faccion2.save(flush: true)

		Faccion faccionAdmins = new Faccion(nombreFaccion: "Faccion Admina", miembros: [])
		faccionAdmins.save(flush: true)

		def ret=[]
		ret.add(new User(username: 'cr.calle', password:'ks3d7fcd8$f1', faccion: faccionAdmins))
		ret.add(new User(username: 'gcortes', password:'ks3d7fcd8$f1', faccion: faccionAdmins))

		def ret1=[]
		for(int i=1;i<6;i++) {
			ret1.add(new User(username: 'pruebamel' + i, password:'12345', faccion: faccion1))
		}
		for(int i=6;i<11;i++) {
			ret1.add(new User(username: 'pruebamel' + i, password:'12345', faccion: faccion2))
		}
		ret.each { retValue ->
			retValue.save(flush: true)
			UserRole.create retValue,roles[0], true
		}
		ret1.each { retValue ->
			retValue.save(flush: true)
			UserRole.create retValue,roles[1], true
			//ret.add(retValue)
		}
		

		return(ret1)
	}

}
