package co.edu.uniandes.login

class Faccion {

	String nombreFaccion;
	int puntos = 0;
	int monedas = 0;
	
	static hasMany = [miembros: User]

    static constraints = {
    }
}
