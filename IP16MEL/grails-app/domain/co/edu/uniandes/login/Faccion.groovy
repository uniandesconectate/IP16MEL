package co.edu.uniandes.login

class Faccion {

	String nombreFaccion;
	int puntos = 0;
	int monedas = 0;
	
	static hasMany = [miembros: User]
	static belongsTo = [seccion: Seccion]

    static constraints = {
		nombreFaccion()
		puntos()
		monedas()
		miembros()
		seccion()
    }
}
