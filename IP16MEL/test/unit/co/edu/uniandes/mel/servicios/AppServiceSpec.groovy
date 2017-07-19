package co.edu.uniandes.mel.servicios

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(AppService)
@Mock([MotorService])
class AppServiceSpec extends Specification
{
    String idEstudiante
    String seccion

    def setup()
    {
        idEstudiante = '13'         // Debe cambiarse para correr las pruebas cada vez, porque no está disponible la eliminación de estudiantes.
        seccion = 'faccion1a'
    }

    def cleanup()
    {

    }

    void "test crear estudiante"()
    {
        when:
        Object[] respuesta = service.crearEstudiante(idEstudiante, idEstudiante, idEstudiante + '@uniandes.edu.co', seccion)

        then:
        respuesta[0] == 'ok'
    }

    void "test traer dashboard del estudiante"()
    {
        when:
        Object[] dashboard = service.getDashboardEstudiante(idEstudiante)

        then:
        dashboard[0] == idEstudiante                            // id estudiante
        dashboard[1] == idEstudiante                            // Nombre estudiante
        dashboard[2] == idEstudiante + '@uniandes.edu.co'       // Correo estudiante
        dashboard[3] == 0                                       // Cantidad de puntos
        dashboard[4] == 0                                       // Cantidad de gemas
        dashboard[5] == [0, 0, 0, 0, 0, 0, 0, 0]                // Cantidad de estrellas por ciclos.
    }

    void "test registrar ciclos mecánicos"()
    {
        when:
        Object[] respuesta = service.registrarCicloMecanico('semana_1', idEstudiante, 300)
        respuesta = service.registrarCicloMecanico('semana_2', idEstudiante, 270)
        respuesta = service.registrarCicloMecanico('semana_3', idEstudiante, 240)
        respuesta = service.registrarCicloMecanico('semana_4', idEstudiante, 180)

        then:
        respuesta[0] == null
    }

    void "test eliminar estudiante"()
    {
        when:
        Object[] respuesta = service.eliminarEstudiante(idEstudiante)

        then:
        respuesta[0] == null    // WS no funciona.
    }
}