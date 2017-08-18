package co.edu.uniandes.login

class Seccion
{
	
	String nombre
	
	static hasMany = [facciones: Faccion, estudiantes: Estudiante]

    static constraints = {
		nombre size: 1..200, blank: false
		facciones()
		estudiantes()
    }
}