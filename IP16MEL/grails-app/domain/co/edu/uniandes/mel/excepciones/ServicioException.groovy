package co.edu.uniandes.mel.excepciones

import grails.plugins.rest.client.RestResponse

class ServicioException extends Exception
{
    public RestResponse resp

    ServicioException(String mensaje, RestResponse resp)
    {
        super(mensaje)
        this.resp = resp
    }

    static constraints = {
    }
}