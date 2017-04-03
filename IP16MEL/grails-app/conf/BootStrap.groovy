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
		def ret=[]
		ret.add(new User(username: 'cr.calle', password:'ks3d7fcd8$f1'))
		ret.add(new User(username: 'gcortes', password:'ks3d7fcd8$f1'))
		ret.each { retValue ->
			retValue.save(flush: true)
			UserRole.create retValue,roles[0], true
		}
		
		def ret1=[]
		ret1.add(new User(username: 'aaa', password:'12345'))
		ret1.each { retValue ->
			retValue.save(flush: true)
			UserRole.create retValue,roles[1], true
			ret.add(retValue)
		}

		return(ret)
	}

}
