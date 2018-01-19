package co.edu.uniandes.login

class Estudiante
{
    User user
    String nombre
    int medallas = 0
    int gemas = 0
    int puntos = 0
    int[] monedasSemanas = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    double[] aporteSemanas = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    int monedas = 0
    static belongsTo = [equipo: Equipo]

    static constraints = {
        equipo nullable: true
    }
}