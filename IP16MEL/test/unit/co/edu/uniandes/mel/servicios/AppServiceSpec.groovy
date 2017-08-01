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
    final static String FACCION = 'test Sección 1 Faccion A'

    def setup()
    {

    }

    def cleanup()
    {

    }

    void "test crear facción"()
    {
        when:
        Map respuesta = service.crearFaccion(FACCION)

        then:
        respuesta['mensaje'] == 'La facción ha sido creada.'
    }

    void "test crear estudiantes"()
    {
        when:
        Map respuesta = service.crearEstudiante('1', '1', '1@uniandes.edu.co', FACCION)
        then:
        respuesta['mensaje'] == 'El estudiante ha sido creado y asignado a la facción.'

        when:
        respuesta = service.crearEstudiante('2', '2', '2@uniandes.edu.co', FACCION)
        then:
        respuesta['mensaje'] == 'El estudiante ha sido creado y asignado a la facción.'

        when:
        respuesta = service.crearEstudiante('3', '3', '3@uniandes.edu.co', FACCION)
        then:
        respuesta['mensaje'] == 'El estudiante ha sido creado y asignado a la facción.'

        when:
        respuesta = service.crearEstudiante('4', '4', '4@uniandes.edu.co', FACCION)
        then:
        respuesta['mensaje'] == 'El estudiante ha sido creado y asignado a la facción.'

        when:
        respuesta = service.crearEstudiante('5', '5', '5@uniandes.edu.co', FACCION)
        then:
        respuesta['mensaje'] == 'El estudiante ha sido creado y asignado a la facción.'

        when:
        respuesta = service.crearEstudiante('6', '6', '6@uniandes.edu.co', FACCION)
        then:
        respuesta['mensaje'] == 'El estudiante ha sido creado y asignado a la facción.'

        when:
        respuesta = service.crearEstudiante('7', '7', '7@uniandes.edu.co', FACCION)
        then:
        respuesta['mensaje'] == 'El estudiante ha sido creado y asignado a la facción.'

        when:
        respuesta = service.crearEstudiante('8', '8', '8@uniandes.edu.co', FACCION)
        then:
        respuesta['mensaje'] == 'El estudiante ha sido creado y asignado a la facción.'
    }

    void "test traer datos del estudiante"()
    {
        when:
        Map respuesta = service.traerDatosEstudiante('1')

        then:
        respuesta['estudiante'].nombre == '1'
        respuesta['estudiante'].puntos == 0
        respuesta['estudiante'].gemas == 0
        respuesta['estudiante'].estrellasSemanas[0] == 0
        respuesta['estudiante'].aporteSemanas[0] == 0
    }

    void "test traer datos de la facción"()
    {
        when:
        Map respuesta = service.traerFaccion(FACCION)

        then:
        respuesta['faccion'].monedas == 0
        respuesta['faccion'].puntos == 0
        respuesta['faccion'].promedioPuntos() == 0
        respuesta['faccion'].promedioMonedas() == 0
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

    void "test traer datos del estudiante después de ciclo mecánico"()
    {
        when:
        Map respuesta = service.traerDatosEstudiante('1')

        then:
        respuesta['estudiante'].puntos == 0
        respuesta['estudiante'].gemas == 1
        respuesta['estudiante'].estrellasSemanas[0] == 5
        respuesta['estudiante'].aporteSemanas[0] == 25

        when:
        respuesta = service.traerDatosEstudiante('2')
        then:
        respuesta['estudiante'].puntos == 0
        respuesta['estudiante'].gemas == 0
        respuesta['estudiante'].estrellasSemanas[0] == 4
        respuesta['estudiante'].aporteSemanas[0] == 20

        when:
        respuesta = service.traerDatosEstudiante('3')
        then:
        respuesta['estudiante'].puntos == 0
        respuesta['estudiante'].gemas == 0
        respuesta['estudiante'].estrellasSemanas[0] == 3
        respuesta['estudiante'].aporteSemanas[0] == 15

        when:
        respuesta = service.traerDatosEstudiante('4')
        then:
        respuesta['estudiante'].puntos == 0
        respuesta['estudiante'].gemas == 0
        respuesta['estudiante'].estrellasSemanas[0] == 2
        respuesta['estudiante'].aporteSemanas[0] == 10

        when:
        respuesta = service.traerDatosEstudiante('5')
        then:
        respuesta['estudiante'].puntos == 0
        respuesta['estudiante'].gemas == 0
        respuesta['estudiante'].estrellasSemanas[0] == 1
        respuesta['estudiante'].aporteSemanas[0] == 5

        when:
        respuesta = service.traerDatosEstudiante('6')
        then:
        respuesta['estudiante'].puntos == 0
        respuesta['estudiante'].gemas == 0
        respuesta['estudiante'].estrellasSemanas[0] == 0
        respuesta['estudiante'].aporteSemanas[0] == 0

        when:
        respuesta = service.traerDatosEstudiante('7')
        then:
        respuesta['estudiante'].puntos == 0
        respuesta['estudiante'].gemas == 0
        respuesta['estudiante'].estrellasSemanas[0] == 4
        respuesta['estudiante'].aporteSemanas[0] == 20

        when:
        respuesta = service.traerDatosEstudiante('8')
        then:
        respuesta['estudiante'].puntos == 0
        respuesta['estudiante'].gemas == 0
        respuesta['estudiante'].estrellasSemanas[0] == 1
        respuesta['estudiante'].aporteSemanas[0] == 5
    }

    void "test traer datos de la facción después de ciclo mecánico"()
    {
        when:
        Map respuesta = service.traerFaccion(FACCION)

        then:
        respuesta['faccion'].monedas == 20
        respuesta['faccion'].puntos == 0
        respuesta['faccion'].promedioMonedas() == 2
        respuesta['faccion'].promedioPuntos() == 0
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

    void "test traer datos del estudiante después de ejercicios cognitivos"()
    {
        when:
        Map respuesta = service.traerDatosEstudiante('1')
        then:
        respuesta['estudiante'].puntos == 3
        respuesta['estudiante'].gemas == 2
        respuesta['estudiante'].medallas == 0
        respuesta['estudiante'].estrellasSemanas[0] == 5

        when:
        respuesta = service.traerDatosEstudiante('2')
        then:
        respuesta['estudiante'].puntos == 8
        respuesta['estudiante'].gemas == 2
        respuesta['estudiante'].medallas == 0
        respuesta['estudiante'].estrellasSemanas[0] == 4

        when:
        respuesta = service.traerDatosEstudiante('3')
        then:
        respuesta['estudiante'].puntos == 20
        respuesta['estudiante'].gemas == 3
        respuesta['estudiante'].medallas == 0
        respuesta['estudiante'].estrellasSemanas[0] == 3

        when:
        respuesta = service.traerDatosEstudiante('6')
        then:
        respuesta['estudiante'].puntos == 40
        respuesta['estudiante'].gemas == 6
        respuesta['estudiante'].medallas == 0
        respuesta['estudiante'].estrellasSemanas[0] == 0
    }

    void "test traer datos de la facción después de ejercicios cognitivos"()
    {
        when:
        Map respuesta = service.traerFaccion(FACCION)

        then:
        respuesta['faccion'].monedas == 20
        respuesta['faccion'].puntos == 71
        respuesta['faccion'].promedioMonedas() == 2
        respuesta['faccion'].promedioPuntos() == 8
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

    void "test traer datos del estudiante después de ejercicios honorificos"()
    {
        when:
        Map respuesta = service.traerDatosEstudiante('3')
        then:
        respuesta['estudiante'].puntos == 50
        respuesta['estudiante'].gemas == 0
        respuesta['estudiante'].medallas == 1
        respuesta['estudiante'].estrellasSemanas[0] == 3

        when:
        respuesta = service.traerDatosEstudiante('6')
        then:
        respuesta['estudiante'].puntos == 40
        respuesta['estudiante'].gemas == 3
        respuesta['estudiante'].medallas == 0
        respuesta['estudiante'].estrellasSemanas[0] == 0
    }

    void "test traer datos de la facción después de ejercicios honoríficos"()
    {
        when:
        Map respuesta = service.traerFaccion(FACCION)

        then:
        respuesta['faccion'].monedas == 20
        respuesta['faccion'].puntos == 101
        respuesta['faccion'].promedioMonedas() == 2
        respuesta['faccion'].promedioPuntos() == 12
    }

    void "test traer comparativo de facciones"()
    {
        when:
        Map respuesta = service.traerSeccion(FACCION)

        then:
        respuesta['seccion'].facciones[0].promedioMonedas() == 2
        respuesta['seccion'].facciones[0].promedioPuntos() == 12
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

    void "test eliminar facción"()
    {
        when:
        Map respuesta = service.eliminarFaccion(FACCION)

        then:
        respuesta['mensaje'] == 'La facción ha sido eliminada.'
    }
}