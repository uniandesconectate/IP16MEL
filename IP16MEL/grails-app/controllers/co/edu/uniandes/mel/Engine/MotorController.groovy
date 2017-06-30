package co.edu.uniandes.mel.Engine

import co.edu.uniandes.mel.excepciones.ServicioException
import grails.plugin.springsecurity.annotation.Secured
import grails.plugins.rest.client.RestResponse
import org.codehaus.groovy.grails.web.json.JSONElement

class MotorController
{
    // Token de app MEL en el motor.
    final static String TOKEN = '210fd18fe01d086fe1f6ed60f789137b'

    def motorService

    @Secured(['ROLE_STUDENT', 'ROLE_ADMIN'])
    def inicio()
    {
        flash.message = ''
        flash.error = ''
        RestResponse resp

        try
        {
            resp = motorService.getDashboardEstudiante(TOKEN, 'serg')
            flash.message = 'OK'
            ['json': resp.json.toString()]
        }
        catch(ServicioException ex)
        {
            flash.error = ex.getMessage()
            ['json': ex.resp.json.toString()]
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
    }

    @Secured(['ROLE_ADMIN'])
    def invocarMotor()
    {
        flash.message = ''
        flash.error = ''
        RestResponse resp
        String accion = 'tarea'

        try
        {
            if(accion == 'tarea') resp = motorService.completarMision(TOKEN, 'serg', 'semana_1', '4,3,5,5')

            render(resp.json.toString())
        }
        catch(ServicioException ex)
        {
            render(ex.resp.json.toString())
        }
        catch(Exception ex)
        {
            render("<h3>Ha ocurrido un error</h3><p>" + ex.getMessage() + "</p>")
        }
    }
}