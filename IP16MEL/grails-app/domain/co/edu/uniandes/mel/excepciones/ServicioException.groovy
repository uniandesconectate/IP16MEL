package co.edu.uniandes.mel.excepciones

/**
 * Permite hacer rollback de la transacción cuando ocurre algún error en la capa de servicios.
 */
class ServicioException extends RuntimeException
{
    ServicioException(String mensaje)
    {
        super(mensaje)
    }

    static constraints = {
    }
}