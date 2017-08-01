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

    int promedioPuntos()
    {
        int promedio

        if(!miembros.isEmpty()) promedio = puntos.intdiv(miembros.size())
        else promedio = 0

        return promedio
    }

    int promedioMonedas()
    {
        int promedio

        if(!miembros.isEmpty()) promedio = monedas.intdiv(miembros.size())
        else promedio = 0

        return promedio
    }
}