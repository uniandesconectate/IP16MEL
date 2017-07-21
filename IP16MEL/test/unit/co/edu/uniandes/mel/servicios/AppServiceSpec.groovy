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
    def setup()
    {

    }

    def cleanup()
    {

    }

    void "test crear sección"()
    {
        when:
        service.crearSeccion('test faccion')

        then:
        true
    }

    void "test crear estudiantes"()
    {
        when:
        (1..5).each{service.crearEstudiante(it.toString(), it.toString(), it.toString() + '@uniandes.edu.co', 'test faccion') }

        then:
        true
    }

    void "test traer dashboard del estudiante"()
    {
        when:
        Map dashboard = service.traerDashboardEstudiante('1')

        then:
        dashboard['idEstudiante'] == '1'
        dashboard['nombreEstudiante'] == '1'
        dashboard['correoEstudiante'] == '1@uniandes.edu.co'
        dashboard['puntosEstudiante'] == 0
        dashboard['gemasEstudiante'] == 0
    }

    void "test registrar ciclo mecánico"()
    {
        when:
        service.registrarCicloMecanico('semana_1', '1', [100] as int[])
        service.registrarCicloMecanico('semana_1', '2', [90] as int[])
        service.registrarCicloMecanico('semana_1', '3', [80] as int[])
        service.registrarCicloMecanico('semana_1', '4', [60] as int[])
        service.registrarCicloMecanico('semana_1', '5', [35] as int[])

        then:
        true
    }

    void "test eliminar estudiantes"()
    {
        when:
        (1..5).each {service.eliminarEstudiante(it.toString())}

        then:
        true
    }

    void "test eliminar sección"()
    {
        when:
        service.eliminarSeccion('test faccion')

        then:
        true
    }
}