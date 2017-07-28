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
        respuesta['mensaje'] == 'La sección ha sido creada.'
    }

    void "test crear estudiantes"()
    {
        when:
        Map respuesta = service.crearEstudiante('1', '1', '1@uniandes.edu.co', 'test faccion')
        then:
        respuesta['mensaje'] == 'El estudiante ha sido creado y asignado a la sección.'

        when:
        respuesta = service.crearEstudiante('2', '2', '2@uniandes.edu.co', 'test faccion')
        then:
        respuesta['mensaje'] == 'El estudiante ha sido creado y asignado a la sección.'

        when:
        respuesta = service.crearEstudiante('3', '3', '3@uniandes.edu.co', 'test faccion')
        then:
        respuesta['mensaje'] == 'El estudiante ha sido creado y asignado a la sección.'

        when:
        respuesta = service.crearEstudiante('4', '4', '4@uniandes.edu.co', 'test faccion')
        then:
        respuesta['mensaje'] == 'El estudiante ha sido creado y asignado a la sección.'

        when:
        respuesta = service.crearEstudiante('5', '5', '5@uniandes.edu.co', 'test faccion')
        then:
        respuesta['mensaje'] == 'El estudiante ha sido creado y asignado a la sección.'

        when:
        respuesta = service.crearEstudiante('6', '6', '6@uniandes.edu.co', 'test faccion')
        then:
        respuesta['mensaje'] == 'El estudiante ha sido creado y asignado a la sección.'

        when:
        respuesta = service.crearEstudiante('7', '7', '7@uniandes.edu.co', 'test faccion')
        then:
        respuesta['mensaje'] == 'El estudiante ha sido creado y asignado a la sección.'

        when:
        respuesta = service.crearEstudiante('8', '8', '8@uniandes.edu.co', 'test faccion')
        then:
        respuesta['mensaje'] == 'El estudiante ha sido creado y asignado a la sección.'
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
        dashboard['aporteEstudiante']['ciclo_1'] == 0
    }

    void "test traer dashboard de la sección"()
    {
        when:
        Map respuesta = service.traerDashboardSeccion('test faccion')

        then:
        respuesta['monedasSeccion'] == 0
        respuesta['puntosSeccion'] == 0
    }

    void "test registrar ciclo mecánico"()
    {
        when:
        Map respuesta = service.registrarCicloMecanico('ciclo_1', '1', [100] as int[])
        then:
        respuesta['mensaje'] == 'El ciclo mecánico ha sido registrado.'

        when:
        respuesta = service.registrarCicloMecanico('ciclo_1', '2', [90] as int[])
        then:
        respuesta['mensaje'] == 'El ciclo mecánico ha sido registrado.'

        when:
        respuesta = service.registrarCicloMecanico('ciclo_1', '3', [80] as int[])
        then:
        respuesta['mensaje'] == 'El ciclo mecánico ha sido registrado.'

        when:
        respuesta = service.registrarCicloMecanico('ciclo_1', '4', [60] as int[])
        then:
        respuesta['mensaje'] == 'El ciclo mecánico ha sido registrado.'

        when:
        respuesta = service.registrarCicloMecanico('ciclo_1', '5', [35] as int[])
        then:
        respuesta['mensaje'] == 'El ciclo mecánico ha sido registrado.'

        when:
        respuesta = service.registrarCicloMecanico('ciclo_1', '6', [10, 20, 30, 5, 15] as int[])
        then:
        respuesta['mensaje'] == 'El ciclo mecánico ha sido registrado.'

        when:
        respuesta = service.registrarCicloMecanico('ciclo_1', '7', [99] as int[])
        then:
        respuesta['mensaje'] == 'El ciclo mecánico ha sido registrado.'

        when:
        respuesta = service.registrarCicloMecanico('ciclo_1', '8', [50,25,60,90,100,30] as int[])
        then:
        respuesta['mensaje'] == 'El ciclo mecánico ha sido registrado.'
    }

    void "test traer dashboard del estudiante después de ciclo mecánico"()
    {
        when:
        Map dashboard = service.traerDashboardEstudiante('1')
        then:
        dashboard['puntosEstudiante'] == 0
        dashboard['gemasEstudiante'] == 1
        dashboard['estrellasEstudiante']['ciclo_1'] == 5
        dashboard['aporteEstudiante']['ciclo_1'] == 25

        when:
        dashboard = service.traerDashboardEstudiante('2')
        then:
        dashboard['puntosEstudiante'] == 0
        dashboard['gemasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 4
        dashboard['aporteEstudiante']['ciclo_1'] == 20

        when:
        dashboard = service.traerDashboardEstudiante('3')
        then:
        dashboard['puntosEstudiante'] == 0
        dashboard['gemasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 3
        dashboard['aporteEstudiante']['ciclo_1'] == 15

        when:
        dashboard = service.traerDashboardEstudiante('4')
        then:
        dashboard['puntosEstudiante'] == 0
        dashboard['gemasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 2
        dashboard['aporteEstudiante']['ciclo_1'] == 10

        when:
        dashboard = service.traerDashboardEstudiante('5')
        then:
        dashboard['puntosEstudiante'] == 0
        dashboard['gemasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 1
        dashboard['aporteEstudiante']['ciclo_1'] == 5

        when:
        dashboard = service.traerDashboardEstudiante('6')
        then:
        dashboard['puntosEstudiante'] == 0
        dashboard['gemasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 0
        dashboard['aporteEstudiante']['ciclo_1'] == 0

        when:
        dashboard = service.traerDashboardEstudiante('7')
        then:
        dashboard['puntosEstudiante'] == 0
        dashboard['gemasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 4
        dashboard['aporteEstudiante']['ciclo_1'] == 20

        when:
        dashboard = service.traerDashboardEstudiante('8')
        then:
        dashboard['puntosEstudiante'] == 0
        dashboard['gemasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 1
        dashboard['aporteEstudiante']['ciclo_1'] == 5
    }

    void "test traer dashboard de la sección después de ciclo mecánico"()
    {
        when:
        Map respuesta = service.traerDashboardSeccion('test faccion')

        then:
        respuesta['monedasSeccion'] == 20
        respuesta['puntosSeccion'] == 0
    }

    void "test registrar ejercicios cognitivos"()
    {
        when:
        Map respuesta = service.registrarEjercicioCognitivo('1', Dificultad.FACIL, 85)
        then:
        respuesta['mensaje'] == 'El estudiante aprobó el ejercicio y fue premiado.'

        when:
        respuesta = service.registrarEjercicioCognitivo('2', Dificultad.INTERMEDIA, 99)
        then:
        respuesta['mensaje'] == 'El estudiante aprobó el ejercicio y fue premiado.'

        when:
        respuesta = service.registrarEjercicioCognitivo('3', Dificultad.DIFICIL, 80)
        then:
        respuesta['mensaje'] == 'El estudiante aprobó el ejercicio y fue premiado.'

        when:
        respuesta = service.registrarEjercicioCognitivo('4', Dificultad.DIFICIL, 0)
        then:
        respuesta['mensaje'] == 'El estudiante reprobó el ejercicio.'

        when:
        respuesta = service.registrarEjercicioCognitivo('5', Dificultad.DIFICIL, 79)
        then:
        respuesta['mensaje'] == 'El estudiante reprobó el ejercicio.'

        when:
        respuesta = service.registrarEjercicioCognitivo('6', Dificultad.DIFICIL, 100)
        then:
        respuesta['mensaje'] == 'El estudiante aprobó el ejercicio y fue premiado.'

        when:
        respuesta = service.registrarEjercicioCognitivo('6', Dificultad.DIFICIL, 99)
        then:
        respuesta['mensaje'] == 'El estudiante aprobó el ejercicio y fue premiado.'
    }

    void "test traer dashboard del estudiante después de ejercicios cognitivos"()
    {
        when:
        Map dashboard = service.traerDashboardEstudiante('1')
        then:
        dashboard['puntosEstudiante'] == 3
        dashboard['gemasEstudiante'] == 2
        dashboard['medallasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 5

        when:
        dashboard = service.traerDashboardEstudiante('2')
        then:
        dashboard['puntosEstudiante'] == 8
        dashboard['gemasEstudiante'] == 2
        dashboard['medallasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 4

        when:
        dashboard = service.traerDashboardEstudiante('3')
        then:
        dashboard['puntosEstudiante'] == 20
        dashboard['gemasEstudiante'] == 3
        dashboard['medallasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 3

        when:
        dashboard = service.traerDashboardEstudiante('6')
        then:
        dashboard['puntosEstudiante'] == 40
        dashboard['gemasEstudiante'] == 6
        dashboard['medallasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 0
    }

    void "test traer dashboard de la sección después de ejercicios cognitivos"()
    {
        when:
        Map respuesta = service.traerDashboardSeccion('test faccion')

        then:
        respuesta['monedasSeccion'] == 20
        respuesta['puntosSeccion'] == 71
    }

    void "test registrar ejercicios honoríficos"()
    {
        when:
        Map respuesta = service.registrarEjercicioHonorifico('3', 80)
        then:
        respuesta['mensaje'] == 'El estudiante aprobó el ejercicio y fue premiado.'

        when:
        String mensaje = shouldFail{service.registrarEjercicioHonorifico('4', 80)}
        then:
        mensaje == 'Hubo un problema al registrar el ejercicio honorífico: Not enough currencies to pay mission fee'

        when:
        respuesta = service.registrarEjercicioHonorifico('6', 79)
        then:
        respuesta['mensaje'] == 'El estudiante reprobó el ejercicio.'

        when:
        mensaje = shouldFail{service.registrarEjercicioHonorifico('6', 80)}
        then:
        mensaje == 'Hubo un problema al registrar el ejercicio honorífico: This mission is not available for this player or has been already accepted'
    }

    void "test traer dashboard del estudiante después de ejercicios honorificos"()
    {
        when:
        Map dashboard = service.traerDashboardEstudiante('3')
        then:
        dashboard['puntosEstudiante'] == 50
        dashboard['gemasEstudiante'] == 0
        dashboard['medallasEstudiante'] == 1
        dashboard['estrellasEstudiante']['ciclo_1'] == 3

        when:
        dashboard = service.traerDashboardEstudiante('6')
        then:
        dashboard['puntosEstudiante'] == 40
        dashboard['gemasEstudiante'] == 3
        dashboard['medallasEstudiante'] == 0
        dashboard['estrellasEstudiante']['ciclo_1'] == 0
    }

    void "test traer dashboard de la sección después de ejercicios honoríficos"()
    {
        when:
        Map respuesta = service.traerDashboardSeccion('test faccion')

        then:
        respuesta['monedasSeccion'] == 20
        respuesta['puntosSeccion'] == 101
    }

    void "test eliminar estudiantes"()
    {
        when:
        Map respuesta = service.eliminarEstudiante('1')
        then:
        respuesta['mensaje'] == 'El estudiante ha sido eliminado.'

        when:
        respuesta = service.eliminarEstudiante('2')
        then:
        respuesta['mensaje'] == 'El estudiante ha sido eliminado.'

        when:
        respuesta = service.eliminarEstudiante('3')
        then:
        respuesta['mensaje'] == 'El estudiante ha sido eliminado.'

        when:
        respuesta = service.eliminarEstudiante('4')
        then:
        respuesta['mensaje'] == 'El estudiante ha sido eliminado.'

        when:
        respuesta = service.eliminarEstudiante('5')
        then:
        respuesta['mensaje'] == 'El estudiante ha sido eliminado.'

        when:
        respuesta = service.eliminarEstudiante('6')
        then:
        respuesta['mensaje'] == 'El estudiante ha sido eliminado.'

        when:
        respuesta = service.eliminarEstudiante('7')
        then:
        respuesta['mensaje'] == 'El estudiante ha sido eliminado.'

        when:
        respuesta = service.eliminarEstudiante('8')
        then:
        respuesta['mensaje'] == 'El estudiante ha sido eliminado.'
    }

    void "test eliminar sección"()
    {
        when:
        Map respuesta = service.eliminarSeccion('test faccion')

        then:
        respuesta['mensaje'] == 'La sección ha sido eliminada.'
    }
}