package co.edu.uniandes.login

class Estudiante
{
    User user
    String nombre
    int medallas=0
    int gemas=0
    int puntos=0
    int[] estrellasQuincenas =[0, 0, 0, 0, 0, 0]
    double[] aporteQuincenas =[0, 0, 0, 0, 0, 0]
    static belongsTo = [faccion: Faccion]

    static constraints = {
        faccion nullable: true
    }
}