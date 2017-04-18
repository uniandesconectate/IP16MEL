import co.edu.uniandes.login.Faccion
import co.edu.uniandes.login.Role
import co.edu.uniandes.login.Seccion
import co.edu.uniandes.login.User;
import co.edu.uniandes.login.UserRole;

class BootStrap {
	def GrailsApplication
	
    def init = { servletContext ->
		def roles=crearRoles()
		def usuarios=crearUsuarios(roles)
		def secciones=crearSecciones(usuarios)
		def facciones=crearFacciones(secciones)
		def estudiantes=crearEstudiantes(facciones, roles)
    }
    def destroy = {
    }
	
	def crearRoles() {
		def ret=[]
		ret.add(new Role(authority: GrailsApplication.config.co.edu.uniandes.login.role.admin))
		ret.add(new Role(authority: GrailsApplication.config.co.edu.uniandes.login.role.student))
		
		ret.each { retValue ->
			retValue.save(flush: true)
		}
		
		return(ret)
	}
	
	def crearUsuarios(def roles) {
		def ret=[]
		def usuarios = ["cr.calle","gcortes","u1","u2","u3","u4","u5","u6"]
		Seccion seccionProf = new Seccion(nombre: "Curso profesores")
		seccionProf.save(flush: true)
		Faccion faccion=new Faccion(nombreFaccion: "Faccion profesores", miembros: [], seccion: seccionProf)
		faccion.save(flush:true)

		usuarios.each { usuario ->
			def usuarioNuevo = new User(username: usuario, password:'ks3d7fcd8$f1', faccion: faccion, nombre: "Nombre " + usuario)
			usuarioNuevo.save(flush: true) 
			UserRole.create usuarioNuevo, roles[0], true
			ret.add(usuarioNuevo)
		}
		return(ret)
	}

	def crearSecciones(def usuarios) {
		def ret=[]
		def secciones = ["s1","s2","s3","s4","s5","s6","s7","s8"]
		for(int i=0;i<secciones.size();i++) {
			Seccion seccion=new Seccion(nombre: secciones[i], profesor: usuarios[i], facciones: [], estudiantes: [])
			seccion.save(flush: true)
			ret.add(seccion)
		}
		return(ret)
	}
	
	def crearFacciones(def secciones) {
		def ret=[]
		def faccionesAB = ["A", "B"]
		secciones.each { seccion ->
			faccionesAB.each { faccionAB -> 
				Faccion faccion = new Faccion(nombreFaccion: "Faccion " + faccionAB + " " + seccion.nombre, miembros: [], seccion: seccion)
				faccion.save(flush: true)
				seccion.facciones.add(faccion)
				ret.add(faccion)
			}
			seccion.save(flush:true)
		}

		return(ret)
	}
	
	def crearEstudiantes(def facciones, def roles) {
		def ret=[]
		int i=1
		int faccion=0
		User user
		for(;i<6;i++) {
			user = new User(username: "pruebamel" + i, password:'12345', faccion: facciones[faccion], nombre: "pruebaMel " + i)
			user.save(flush: true)
			UserRole.create user, roles[1], true
			facciones[faccion].seccion.estudiantes.add(user)
			facciones[faccion].seccion.save(flush:true)
		}
		faccion++
		for(;i<11;i++) {
			user = new User(username: "pruebamel" + i, password:'12345', faccion: facciones[faccion], nombre: "pruebaMel " + i)
			user.save(flush: true)
			UserRole.create user, roles[1], true
			facciones[faccion].seccion.estudiantes.add(user)
			facciones[faccion].seccion.save(flush:true)
		}
		return(ret)
	}

}
