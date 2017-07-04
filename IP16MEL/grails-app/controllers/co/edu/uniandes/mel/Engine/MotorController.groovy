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

    @Secured(['ROLE_ADMIN'])
    def fachada()
    {

    }

    @Secured(['ROLE_ADMIN'])
    def invocarMotor()
    {
        flash.message = ''
        flash.error = ''
        RestResponse resp
        String accion

        try
        {
            accion = params.accion

            if(accion == 'traer_datos_estudiante') resp = motorService.getPlayerData(TOKEN, 'serg')
            else if(accion == 'completar_ejercicio_mecanico') resp = motorService.completeMission(TOKEN, 'semana_1', 'serg', '4,3,5,5', '')
            else if(accion == 'traer_datos_faccion') resp = motorService.getTeamData(TOKEN, 'prueba')
            else if(accion == 'agregar_estudiante_a_faccion') resp = motorService.joinTeam(TOKEN, 'prueba', 'serg')

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