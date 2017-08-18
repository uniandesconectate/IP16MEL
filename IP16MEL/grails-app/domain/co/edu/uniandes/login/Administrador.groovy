package co.edu.uniandes.login

class Administrador
{
    User user
    String nombre

    static hasMany = [secciones: Seccion]

    static constraints = {
    }
}