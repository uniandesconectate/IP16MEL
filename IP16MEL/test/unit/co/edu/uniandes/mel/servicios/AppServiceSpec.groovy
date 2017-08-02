package co.edu.uniandes.mel.servicios

import co.edu.uniandes.login.Faccion
import co.edu.uniandes.login.Seccion
import co.edu.uniandes.login.User
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
        String mensaje = service.crearFaccion(FACCION)

        then:
        mensaje == 'La facción ha sido creada.'
    }

    void "test crear estudiantes"()
    {
        when:
        String mensaje = service.crearEstudiante('1', '1', '1@uniandes.edu.co', FACCION)
        then:
        mensaje == 'El estudiante ha sido creado y asignado a la facción.'

        when:
        mensaje = service.crearEstudiante('2', '2', '2@uniandes.edu.co', FACCION)
        then:
        mensaje == 'El estudiante ha sido creado y asignado a la facción.'

        when:
        mensaje = service.crearEstudiante('3', '3', '3@uniandes.edu.co', FACCION)
        then:
        mensaje == 'El estudiante ha sido creado y asignado a la facción.'

        when:
        mensaje = service.crearEstudiante('4', '4', '4@uniandes.edu.co', FACCION)
        then:
        mensaje == 'El estudiante ha sido creado y asignado a la facción.'

        when:
        mensaje = service.crearEstudiante('5', '5', '5@uniandes.edu.co', FACCION)
        then:
        mensaje == 'El estudiante ha sido creado y asignado a la facción.'

        when:
        mensaje = service.crearEstudiante('6', '6', '6@uniandes.edu.co', FACCION)
        then:
        mensaje == 'El estudiante ha sido creado y asignado a la facción.'

        when:
        mensaje = service.crearEstudiante('7', '7', '7@uniandes.edu.co', FACCION)
        then:
        mensaje == 'El estudiante ha sido creado y asignado a la facción.'

        when:
        mensaje = service.crearEstudiante('8', '8', '8@uniandes.edu.co', FACCION)
        then:
        mensaje == 'El estudiante ha sido creado y asignado a la facción.'
    }

    void "test traer datos del estudiante"()
    {
        when:
        User estudiante = service.traerDatosEstudiante('1')

        then:
        estudiante.nombre == '1'
        estudiante.puntos == 0
        estudiante.gemas == 0
        estudiante.estrellasSemanas[0] == 0
        estudiante.aporteSemanas[0] == 0
    }

    void "test traer datos de la facción"()
    {
        when:
        Faccion faccion = service.traerFaccion(FACCION)

        then:
        faccion.monedas == 0
        faccion.puntos == 0
        faccion.promedioPuntos() == 0
        faccion.promedioMonedas() == 0
    }

    void "test registrar ciclo mecánico"()
    {
        when:
        String mensaje = service.registrarCicloMecanico('ciclo_1', '1', [100] as int[])
        then:
        mensaje == 'El ciclo mecánico ha sido registrado.'

        when:
        mensaje = service.registrarCicloMecanico('ciclo_1', '2', [90] as int[])
        then:
        mensaje == 'El ciclo mecánico ha sido registrado.'

        when:
        mensaje = service.registrarCicloMecanico('ciclo_1', '3', [80] as int[])
        then:
        mensaje == 'El ciclo mecánico ha sido registrado.'

        when:
        mensaje = service.registrarCicloMecanico('ciclo_1', '4', [60] as int[])
        then:
        mensaje == 'El ciclo mecánico ha sido registrado.'

        when:
        mensaje = service.registrarCicloMecanico('ciclo_1', '5', [35] as int[])
        then:
        mensaje == 'El ciclo mecánico ha sido registrado.'

        when:
        mensaje = service.registrarCicloMecanico('ciclo_1', '6', [10, 20, 30, 5, 15] as int[])
        then:
        mensaje == 'El ciclo mecánico ha sido registrado.'

        when:
        mensaje = service.registrarCicloMecanico('ciclo_1', '7', [99] as int[])
        then:
        mensaje == 'El ciclo mecánico ha sido registrado.'

        when:
        mensaje = service.registrarCicloMecanico('ciclo_1', '8', [50,25,60,90,100,30] as int[])
        then:
        mensaje == 'El ciclo mecánico ha sido registrado.'
    }

    void "test traer datos del estudiante después de ciclo mecánico"()
    {
        when:
        User estudiante = service.traerDatosEstudiante('1')

        then:
        estudiante.puntos == 0
        estudiante.gemas == 1
        estudiante.estrellasSemanas[0] == 5
        estudiante.aporteSemanas[0] == 25

        when:
        estudiante = service.traerDatosEstudiante('2')
        then:
        estudiante.puntos == 0
        estudiante.gemas == 0
        estudiante.estrellasSemanas[0] == 4
        estudiante.aporteSemanas[0] == 20

        when:
        estudiante = service.traerDatosEstudiante('3')
        then:
        estudiante.puntos == 0
        estudiante.gemas == 0
        estudiante.estrellasSemanas[0] == 3
        estudiante.aporteSemanas[0] == 15

        when:
        estudiante = service.traerDatosEstudiante('4')
        then:
        estudiante.puntos == 0
        estudiante.gemas == 0
        estudiante.estrellasSemanas[0] == 2
        estudiante.aporteSemanas[0] == 10

        when:
        estudiante = service.traerDatosEstudiante('5')
        then:
        estudiante.puntos == 0
        estudiante.gemas == 0
        estudiante.estrellasSemanas[0] == 1
        estudiante.aporteSemanas[0] == 5

        when:
        estudiante = service.traerDatosEstudiante('6')
        then:
        estudiante.puntos == 0
        estudiante.gemas == 0
        estudiante.estrellasSemanas[0] == 0
        estudiante.aporteSemanas[0] == 0

        when:
        estudiante = service.traerDatosEstudiante('7')
        then:
        estudiante.puntos == 0
        estudiante.gemas == 0
        estudiante.estrellasSemanas[0] == 4
        estudiante.aporteSemanas[0] == 20

        when:
        estudiante = service.traerDatosEstudiante('8')
        then:
        estudiante.puntos == 0
        estudiante.gemas == 0
        estudiante.estrellasSemanas[0] == 1
        estudiante.aporteSemanas[0] == 5
    }

    void "test traer datos de la facción después de ciclo mecánico"()
    {
        when:
        Faccion faccion = service.traerFaccion(FACCION)

        then:
        faccion.monedas == 20
        faccion.puntos == 0
        faccion.promedioMonedas() == 2
        faccion.promedioPuntos() == 0
    }

    void "test registrar ejercicios cognitivos"()
    {
        when:
        String mensaje = service.registrarEjercicioCognitivo('1', Dificultad.FACIL, 85)
        then:
        mensaje == 'El estudiante aprobó el ejercicio y fue premiado.'

        when:
        mensaje = service.registrarEjercicioCognitivo('2', Dificultad.INTERMEDIA, 99)
        then:
        mensaje == 'El estudiante aprobó el ejercicio y fue premiado.'

        when:
        mensaje = service.registrarEjercicioCognitivo('3', Dificultad.DIFICIL, 80)
        then:
        mensaje == 'El estudiante aprobó el ejercicio y fue premiado.'

        when:
        mensaje = service.registrarEjercicioCognitivo('4', Dificultad.DIFICIL, 0)
        then:
        mensaje == 'El estudiante reprobó el ejercicio.'

        when:
        mensaje = service.registrarEjercicioCognitivo('5', Dificultad.DIFICIL, 79)
        then:
        mensaje == 'El estudiante reprobó el ejercicio.'

        when:
        mensaje = service.registrarEjercicioCognitivo('6', Dificultad.DIFICIL, 100)
        then:
        mensaje == 'El estudiante aprobó el ejercicio y fue premiado.'

        when:
        mensaje = service.registrarEjercicioCognitivo('6', Dificultad.DIFICIL, 99)
        then:
        mensaje == 'El estudiante aprobó el ejercicio y fue premiado.'
    }

    void "test traer datos del estudiante después de ejercicios cognitivos"()
    {
        when:
        User estudiante = service.traerDatosEstudiante('1')
        then:
        estudiante.puntos == 3
        estudiante.gemas == 2
        estudiante.medallas == 0
        estudiante.estrellasSemanas[0] == 5

        when:
        estudiante = service.traerDatosEstudiante('2')
        then:
        estudiante.puntos == 8
        estudiante.gemas == 2
        estudiante.medallas == 0
        estudiante.estrellasSemanas[0] == 4

        when:
        estudiante = service.traerDatosEstudiante('3')
        then:
        estudiante.puntos == 20
        estudiante.gemas == 3
        estudiante.medallas == 0
        estudiante.estrellasSemanas[0] == 3

        when:
        estudiante = service.traerDatosEstudiante('6')
        then:
        estudiante.puntos == 40
        estudiante.gemas == 6
        estudiante.medallas == 0
        estudiante.estrellasSemanas[0] == 0
    }

    void "test traer datos de la facción después de ejercicios cognitivos"()
    {
        when:
        Faccion faccion = service.traerFaccion(FACCION)

        then:
        faccion.monedas == 20
        faccion.puntos == 71
        faccion.promedioMonedas() == 2
        faccion.promedioPuntos() == 8
    }

    void "test registrar ejercicios honoríficos"()
    {
        when:
        String mensaje = service.registrarEjercicioHonorifico('3', 80)
        then:
        mensaje == 'El estudiante aprobó el ejercicio y fue premiado.'

        when:
        mensaje = shouldFail{service.registrarEjercicioHonorifico('4', 80)}
        then:
        mensaje == 'Hubo un problema al registrar el ejercicio honorífico: Not enough currencies to pay mission fee'

        when:
        mensaje = service.registrarEjercicioHonorifico('6', 79)
        then:
        mensaje == 'El estudiante reprobó el ejercicio.'

        when:
        mensaje = shouldFail{service.registrarEjercicioHonorifico('6', 80)}
        then:
        mensaje == 'Hubo un problema al registrar el ejercicio honorífico: This mission is not available for this player or has been already accepted'
    }

    void "test traer datos del estudiante después de ejercicios honorificos"()
    {
        when:
        User estudiante = service.traerDatosEstudiante('3')
        then:
        estudiante.puntos == 50
        estudiante.gemas == 0
        estudiante.medallas == 1
        estudiante.estrellasSemanas[0] == 3

        when:
        estudiante = service.traerDatosEstudiante('6')
        then:
        estudiante.puntos == 40
        estudiante.gemas == 3
        estudiante.medallas == 0
        estudiante.estrellasSemanas[0] == 0
    }

    void "test traer datos de la facción después de ejercicios honoríficos"()
    {
        when:
        Faccion faccion = service.traerFaccion(FACCION)

        then:
        faccion.monedas == 20
        faccion.puntos == 101
        faccion.promedioMonedas() == 2
        faccion.promedioPuntos() == 12
    }

    void "test traer comparativo de facciones"()
    {
        when:
        Seccion seccion = service.traerSeccion(FACCION)

        then:
        seccion.facciones[0].promedioMonedas() == 2
        seccion.facciones[0].promedioPuntos() == 12
    }

    void "test eliminar estudiantes"()
    {
        when:
        String mensaje = service.eliminarEstudiante('1')
        then:
        mensaje == 'El estudiante ha sido eliminado.'

        when:
        mensaje = service.eliminarEstudiante('2')
        then:
        mensaje == 'El estudiante ha sido eliminado.'

        when:
        mensaje = service.eliminarEstudiante('3')
        then:
        mensaje == 'El estudiante ha sido eliminado.'

        when:
        mensaje = service.eliminarEstudiante('4')
        then:
        mensaje == 'El estudiante ha sido eliminado.'

        when:
        mensaje = service.eliminarEstudiante('5')
        then:
        mensaje == 'El estudiante ha sido eliminado.'

        when:
        mensaje = service.eliminarEstudiante('6')
        then:
        mensaje == 'El estudiante ha sido eliminado.'

        when:
        mensaje = service.eliminarEstudiante('7')
        then:
        mensaje == 'El estudiante ha sido eliminado.'

        when:
        mensaje = service.eliminarEstudiante('8')
        then:
        mensaje == 'El estudiante ha sido eliminado.'
    }

    void "test eliminar facción"()
    {
        when:
        String mensaje = service.eliminarFaccion(FACCION)

        then:
        mensaje == 'La facción ha sido eliminada.'
    }
}