package co.edu.uniandes.mel.servicios

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import co.edu.uniandes.mel.servicios.AppService.Dificultad

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
        Map respuesta = service.crearSeccion('test faccion')

        then:
        respuesta['success']
    }

    void "test crear estudiantes"()
    {
        when:
        (1..8).each{service.crearEstudiante(it.toString(), it.toString(), it.toString() + '@uniandes.edu.co', 'test faccion') }

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
        dashboard['estrellasEstudiante']['ciclo_1'] == 0
    }

    void "test registrar ciclo mecánico"()
    {
        when:
        service.registrarCicloMecanico('ciclo_1', '1', [100] as int[])
        service.registrarCicloMecanico('ciclo_1', '2', [90] as int[])
        service.registrarCicloMecanico('ciclo_1', '3', [80] as int[])
        service.registrarCicloMecanico('ciclo_1', '4', [60] as int[])
        service.registrarCicloMecanico('ciclo_1', '5', [35] as int[])
        service.registrarCicloMecanico('ciclo_1', '6', [10, 20, 30, 5, 15] as int[])
        service.registrarCicloMecanico('ciclo_1', '7', [99] as int[])
        service.registrarCicloMecanico('ciclo_1', '8', [50,25,60,90,100,30] as int[])

        then:
        true
    }

    void "test traer dashboard del estudiante después de ciclo mecánico"()
    {
        when:
        Map dashboard = service.traerDashboardEstudiante('1')
        then: 'Estudiante 1 debió ganar gema por puntaje perfecto y 5 estrellas para su sección.'
        dashboard['puntosEstudiante'] == 0
        dashboard['gemasEstudiante'] == 1
        dashboard['estrellasEstudiante']['ciclo_1'] == 5

        when:
        dashboard = service.traerDashboardEstudiante('2')
        then:
        dashboard['puntosEstudiante'] == 0
        dashboard['gemasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 4

        when:
        dashboard = service.traerDashboardEstudiante('3')
        then:
        dashboard['puntosEstudiante'] == 0
        dashboard['gemasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 3

        when:
        dashboard = service.traerDashboardEstudiante('4')
        then:
        dashboard['puntosEstudiante'] == 0
        dashboard['gemasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 2

        when:
        dashboard = service.traerDashboardEstudiante('5')
        then:
        dashboard['puntosEstudiante'] == 0
        dashboard['gemasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 1

        when:
        dashboard = service.traerDashboardEstudiante('6')
        then:
        dashboard['puntosEstudiante'] == 0
        dashboard['gemasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 0

        when:
        dashboard = service.traerDashboardEstudiante('7')
        then:
        dashboard['puntosEstudiante'] == 0
        dashboard['gemasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 4

        when:
        dashboard = service.traerDashboardEstudiante('8')
        then:
        dashboard['puntosEstudiante'] == 0
        dashboard['gemasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 1
    }

    void "test verificar monedas sección después de ciclo mecánico"()
    {
        when:
        Map respuesta = service.traerDashboardSeccion('test faccion')

        then:
        respuesta['monedasSeccion'] == 20
    }

    void "test registrar ejercicios cognitivos"()
    {
        when:
        service.registrarEjercicioCognitivo('1', Dificultad.FACIL, 85)
        service.registrarEjercicioCognitivo('2', Dificultad.INTERMEDIA, 99)
        service.registrarEjercicioCognitivo('3', Dificultad.DIFICIL, 80)
        service.registrarEjercicioCognitivo('4', Dificultad.DIFICIL, 0)
        service.registrarEjercicioCognitivo('5', Dificultad.DIFICIL, 79)

        then:
        true
    }

    void "test traer dashboard del estudiante después de ejercicios cognitivos"()
    {
        when:
        Map dashboard = service.traerDashboardEstudiante('1')
        then:
        dashboard['puntosEstudiante'] == 3
        dashboard['gemasEstudiante'] == 2
        dashboard['estrellasEstudiante']['ciclo_1'] == 5

        when:
        dashboard = service.traerDashboardEstudiante('2')
        then:
        dashboard['puntosEstudiante'] == 8
        dashboard['gemasEstudiante'] == 2
        dashboard['estrellasEstudiante']['ciclo_1'] == 4

        when:
        dashboard = service.traerDashboardEstudiante('3')
        then:
        dashboard['puntosEstudiante'] == 20
        dashboard['gemasEstudiante'] == 3
        dashboard['estrellasEstudiante']['ciclo_1'] == 3
    }

    void "test verificar monedas sección después de ejercicios cognitivos"()
    {
        when:
        Map respuesta = service.traerDashboardSeccion('test faccion')

        then:
        respuesta['monedasSeccion'] == 20
    }

    void "test eliminar estudiantes"()
    {
        when:
        (1..8).each {service.eliminarEstudiante(it.toString())}

        then:
        true
    }

    void "test eliminar sección"()
    {
        when:
        Map respuesta = service.eliminarSeccion('test faccion')

        then:
        respuesta['status'] == 'This team was destroyed'
    }
}