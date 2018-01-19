package co.edu.uniandes.login

class Seccion
{
	
	String nombre
	
	static hasMany = [equipos: Equipo, estudiantes: Estudiante]

    static constraints = {
		nombre size: 1..200, blank: false
		equipos()
		estudiantes()
    }
}