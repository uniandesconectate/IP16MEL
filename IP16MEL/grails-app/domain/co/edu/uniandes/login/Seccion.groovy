package co.edu.uniandes.login

class Seccion {
	
	String nombre
	User profesor
	
	static hasMany = [facciones: Faccion, estudiantes: User]

    static constraints = {
		nombre size: 1..200, blank: false
		profesor nullable: true
		facciones()
		estudiantes()
    }
}
