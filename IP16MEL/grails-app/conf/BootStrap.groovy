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
		def usuarios = ["ma.de1295","dm.martinez831","ca.escobar2434","cr.calle", "gcortes", "fj.moreno"]
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
		def secciones = ["Secci\u00F3n 1","Secci\u00F3n 2","Secci\u00F3n 3","Secci\u00F3n 4","Secci\u00F3n 5","Secci\u00F3n 6","Secci\u00F3n 7 y 8"]
		def profesores = [0,0,1,1,0,0,2,2]
		for(int i=0;i<secciones.size();i++) {
			Seccion seccion=new Seccion(nombre: secciones[i], profesor: usuarios[profesores[i]], facciones: [], estudiantes: [])
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
		User user
		user = new User(username: 'js.torres1', password:'L4m3nt0B0l', faccion: facciones[0], nombre: 'js.torres1')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'la.ruiz967', password:'L4m3nt0B0l', faccion: facciones[1], nombre: 'la.ruiz967')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'kk.penaranda', password:'L4m3nt0B0l', faccion: facciones[0], nombre: 'kk.penaranda')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'jc.sanguino10', password:'L4m3nt0B0l', faccion: facciones[1], nombre: 'jc.sanguino10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'fc.alvarez10', password:'L4m3nt0B0l', faccion: facciones[1], nombre: 'fc.alvarez10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ca.guerrero', password:'L4m3nt0B0l', faccion: facciones[0], nombre: 'ca.guerrero')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'n.vargas13', password:'L4m3nt0B0l', faccion: facciones[0], nombre: 'n.vargas13')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ma.puentes', password:'L4m3nt0B0l', faccion: facciones[1], nombre: 'ma.puentes')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'c.mendez11', password:'L4m3nt0B0l', faccion: facciones[0], nombre: 'c.mendez11')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'c.santacruza', password:'L4m3nt0B0l', faccion: facciones[1], nombre: 'c.santacruza')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ks.estupinan', password:'L4m3nt0B0l', faccion: facciones[1], nombre: 'ks.estupinan')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'jo.ferrer', password:'L4m3nt0B0l', faccion: facciones[0], nombre: 'jo.ferrer')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'am.valero10', password:'L4m3nt0B0l', faccion: facciones[0], nombre: 'am.valero10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'af.valencia11', password:'L4m3nt0B0l', faccion: facciones[1], nombre: 'af.valencia11')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'n.sanabria', password:'L4m3nt0B0l', faccion: facciones[2], nombre: 'n.sanabria')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'c.cardenas', password:'L4m3nt0B0l', faccion: facciones[3], nombre: 'c.cardenas')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'nf.ortiz', password:'L4m3nt0B0l', faccion: facciones[2], nombre: 'nf.ortiz')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'jr.restom10', password:'L4m3nt0B0l', faccion: facciones[3], nombre: 'jr.restom10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'af.solano10', password:'L4m3nt0B0l', faccion: facciones[2], nombre: 'af.solano10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'di.romero', password:'L4m3nt0B0l', faccion: facciones[3], nombre: 'di.romero')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'js.diazr', password:'L4m3nt0B0l', faccion: facciones[2], nombre: 'js.diazr')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'lc.garavito', password:'L4m3nt0B0l', faccion: facciones[3], nombre: 'lc.garavito')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'n.pachon1231', password:'L4m3nt0B0l', faccion: facciones[2], nombre: 'n.pachon1231')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ca.mendoza', password:'L4m3nt0B0l', faccion: facciones[3], nombre: 'ca.mendoza')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'm.saravia', password:'L4m3nt0B0l', faccion: facciones[3], nombre: 'm.saravia')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'dl.avendano', password:'L4m3nt0B0l', faccion: facciones[2], nombre: 'dl.avendano')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'hs.hernandez', password:'L4m3nt0B0l', faccion: facciones[2], nombre: 'hs.hernandez')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'pa.molina', password:'L4m3nt0B0l', faccion: facciones[3], nombre: 'pa.molina')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'df.machado10', password:'L4m3nt0B0l', faccion: facciones[2], nombre: 'df.machado10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'jp.campos', password:'L4m3nt0B0l', faccion: facciones[3], nombre: 'jp.campos')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'n.sotelo', password:'L4m3nt0B0l', faccion: facciones[2], nombre: 'n.sotelo')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'mi.carrascal', password:'L4m3nt0B0l', faccion: facciones[3], nombre: 'mi.carrascal')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ac.beltrans', password:'L4m3nt0B0l', faccion: facciones[3], nombre: 'ac.beltrans')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ra.valencia10', password:'L4m3nt0B0l', faccion: facciones[2], nombre: 'ra.valencia10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'na.alvarado', password:'L4m3nt0B0l', faccion: facciones[4], nombre: 'na.alvarado')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 's.beltran', password:'L4m3nt0B0l', faccion: facciones[5], nombre: 's.beltran')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'hd.castellanos', password:'L4m3nt0B0l', faccion: facciones[4], nombre: 'hd.castellanos')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'vm.garciad', password:'L4m3nt0B0l', faccion: facciones[4], nombre: 'vm.garciad')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'js.garcial1', password:'L4m3nt0B0l', faccion: facciones[4], nombre: 'js.garcial1')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ld.grosso10', password:'L4m3nt0B0l', faccion: facciones[4], nombre: 'ld.grosso10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'me.hurtado', password:'L4m3nt0B0l', faccion: facciones[4], nombre: 'me.hurtado')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'm.leona', password:'L4m3nt0B0l', faccion: facciones[5], nombre: 'm.leona')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'os.pachon10', password:'L4m3nt0B0l', faccion: facciones[4], nombre: 'os.pachon10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'js.palacios437', password:'L4m3nt0B0l', faccion: facciones[5], nombre: 'js.palacios437')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'da.ramos', password:'L4m3nt0B0l', faccion: facciones[5], nombre: 'da.ramos')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'd.rocha', password:'L4m3nt0B0l', faccion: facciones[5], nombre: 'd.rocha')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'jd.rojas13', password:'L4m3nt0B0l', faccion: facciones[5], nombre: 'jd.rojas13')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'px.romano10', password:'L4m3nt0B0l', faccion: facciones[5], nombre: 'px.romano10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'a.silvag', password:'L4m3nt0B0l', faccion: facciones[5], nombre: 'a.silvag')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'pa.suarezm', password:'L4m3nt0B0l', faccion: facciones[4], nombre: 'pa.suarezm')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'a.trujilloa1', password:'L4m3nt0B0l', faccion: facciones[7], nombre: 'a.trujilloa1')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ax.rodriguez10', password:'L4m3nt0B0l', faccion: facciones[6], nombre: 'ax.rodriguez10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ca.beltran10', password:'L4m3nt0B0l', faccion: facciones[6], nombre: 'ca.beltran10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'df.gonzalez13', password:'L4m3nt0B0l', faccion: facciones[6], nombre: 'df.gonzalez13')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'dm.guatibonza', password:'L4m3nt0B0l', faccion: facciones[6], nombre: 'dm.guatibonza')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'g.ospinaa', password:'L4m3nt0B0l', faccion: facciones[6], nombre: 'g.ospinaa')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'jc.pineda3099', password:'L4m3nt0B0l', faccion: facciones[7], nombre: 'jc.pineda3099')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'jc.ruiz11', password:'L4m3nt0B0l', faccion: facciones[7], nombre: 'jc.ruiz11')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'js.cabra', password:'L4m3nt0B0l', faccion: facciones[7], nombre: 'js.cabra')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'js.martinez', password:'L4m3nt0B0l', faccion: facciones[6], nombre: 'js.martinez')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'js.misas10', password:'L4m3nt0B0l', faccion: facciones[6], nombre: 'js.misas10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'js.ortiz', password:'L4m3nt0B0l', faccion: facciones[7], nombre: 'js.ortiz')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ma.hoyosr', password:'L4m3nt0B0l', faccion: facciones[7], nombre: 'ma.hoyosr')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'na.morenoe', password:'L4m3nt0B0l', faccion: facciones[6], nombre: 'na.morenoe')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 's.naranjop1', password:'L4m3nt0B0l', faccion: facciones[7], nombre: 's.naranjop1')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'sa.yepes', password:'L4m3nt0B0l', faccion: facciones[7], nombre: 'sa.yepes')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'd.montoyav', password:'L4m3nt0B0l', faccion: facciones[8], nombre: 'd.montoyav')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'af.varon', password:'L4m3nt0B0l', faccion: facciones[9], nombre: 'af.varon')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'c.martinezc1', password:'L4m3nt0B0l', faccion: facciones[9], nombre: 'c.martinezc1')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'jp.patarroyo10', password:'L4m3nt0B0l', faccion: facciones[9], nombre: 'jp.patarroyo10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'f.escobar', password:'L4m3nt0B0l', faccion: facciones[8], nombre: 'f.escobar')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'v.chacon', password:'L4m3nt0B0l', faccion: facciones[9], nombre: 'v.chacon')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'a.eslava', password:'L4m3nt0B0l', faccion: facciones[8], nombre: 'a.eslava')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'af.losada', password:'L4m3nt0B0l', faccion: facciones[9], nombre: 'af.losada')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ys.tarazona', password:'L4m3nt0B0l', faccion: facciones[9], nombre: 'ys.tarazona')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'c.gomezs', password:'L4m3nt0B0l', faccion: facciones[8], nombre: 'c.gomezs')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'jp.carreno', password:'L4m3nt0B0l', faccion: facciones[8], nombre: 'jp.carreno')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'a.caicedom', password:'L4m3nt0B0l', faccion: facciones[8], nombre: 'a.caicedom')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ce.quintero', password:'L4m3nt0B0l', faccion: facciones[9], nombre: 'ce.quintero')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'cv.trujillo', password:'L4m3nt0B0l', faccion: facciones[8], nombre: 'cv.trujillo')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'jm.contreras10', password:'L4m3nt0B0l', faccion: facciones[8], nombre: 'jm.contreras10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'mp.bayonal', password:'L4m3nt0B0l', faccion: facciones[9], nombre: 'mp.bayonal')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'je.vivas', password:'L4m3nt0B0l', faccion: facciones[8], nombre: 'je.vivas')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ga.bejarano10', password:'L4m3nt0B0l', faccion: facciones[9], nombre: 'ga.bejarano10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ce.robles', password:'L4m3nt0B0l', faccion: facciones[8], nombre: 'ce.robles')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'jpr.arango10', password:'L4m3nt0B0l', faccion: facciones[9], nombre: 'jpr.arango10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'af.fajardo10', password:'L4m3nt0B0l', faccion: facciones[8], nombre: 'af.fajardo10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'm.devia', password:'L4m3nt0B0l', faccion: facciones[10], nombre: 'm.devia')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'lm.gonzalezf', password:'L4m3nt0B0l', faccion: facciones[11], nombre: 'lm.gonzalezf')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'id.salazar', password:'L4m3nt0B0l', faccion: facciones[10], nombre: 'id.salazar')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'jc.castaneda1', password:'L4m3nt0B0l', faccion: facciones[11], nombre: 'jc.castaneda1')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'jf.jaime10', password:'L4m3nt0B0l', faccion: facciones[10], nombre: 'jf.jaime10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ka.babativa', password:'L4m3nt0B0l', faccion: facciones[11], nombre: 'ka.babativa')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ca.giraldof', password:'L4m3nt0B0l', faccion: facciones[11], nombre: 'ca.giraldof')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'dn.gonzalez', password:'L4m3nt0B0l', faccion: facciones[11], nombre: 'dn.gonzalez')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'm.moreno', password:'L4m3nt0B0l', faccion: facciones[10], nombre: 'm.moreno')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 's.rangel', password:'L4m3nt0B0l', faccion: facciones[11], nombre: 's.rangel')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'g.marcelo10', password:'L4m3nt0B0l', faccion: facciones[10], nombre: 'g.marcelo10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'jf.copete', password:'L4m3nt0B0l', faccion: facciones[11], nombre: 'jf.copete')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 's.abadia', password:'L4m3nt0B0l', faccion: facciones[10], nombre: 's.abadia')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'by.cuta10', password:'L4m3nt0B0l', faccion: facciones[11], nombre: 'by.cuta10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'da.solano1', password:'L4m3nt0B0l', faccion: facciones[10], nombre: 'da.solano1')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ne.ortega', password:'L4m3nt0B0l', faccion: facciones[11], nombre: 'ne.ortega')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'cs.gomez', password:'L4m3nt0B0l', faccion: facciones[10], nombre: 'cs.gomez')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ch.patino', password:'L4m3nt0B0l', faccion: facciones[10], nombre: 'ch.patino')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'f.velasquez', password:'L4m3nt0B0l', faccion: facciones[12], nombre: 'f.velasquez')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'm.velezm', password:'L4m3nt0B0l', faccion: facciones[12], nombre: 'm.velezm')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'f.marroquin10', password:'L4m3nt0B0l', faccion: facciones[12], nombre: 'f.marroquin10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'ma.gomezc', password:'L4m3nt0B0l', faccion: facciones[12], nombre: 'ma.gomezc')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'jr.pacheco10', password:'L4m3nt0B0l', faccion: facciones[12], nombre: 'jr.pacheco10')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 's.blancoc', password:'L4m3nt0B0l', faccion: facciones[12], nombre: 's.blancoc')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'n.gaitan', password:'L4m3nt0B0l', faccion: facciones[13], nombre: 'n.gaitan')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'n.bello', password:'L4m3nt0B0l', faccion: facciones[13], nombre: 'n.bello')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'jd.arango', password:'L4m3nt0B0l', faccion: facciones[13], nombre: 'jd.arango')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'js.vacat', password:'L4m3nt0B0l', faccion: facciones[13], nombre: 'js.vacat')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 's.gutierrezc1', password:'L4m3nt0B0l', faccion: facciones[13], nombre: 's.gutierrezc1')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'js.gordillo', password:'L4m3nt0B0l', faccion: facciones[13], nombre: 'js.gordillo')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
		
		user = new User(username: 'de.gutierrez', password:'L4m3nt0B0l', faccion: facciones[13], nombre: 'de.gutierrez')
		user.faccion.miembros.add(user)
		user.save(flush: true)
		user.faccion.save(flush:true)
		user.faccion.seccion.estudiantes.add(user)
		user.faccion.seccion.save(flush:true)
		UserRole.create user, roles[1], true
		ret.add(user)
  

		return(ret)
	}

}
