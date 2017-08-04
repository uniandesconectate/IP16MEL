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
    final static String MECANICA = 'mq1'
    final static String COGNITIVA_FACIL = 'cfq1'
    final static String COGNITIVA_INTERMEDIA = 'cmq1'
    final static String COGNITIVA_DIFICIL = 'cdq1'
    final static String HONORIFICA = 'hq1'

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

    void "test registrar pruebas mecánicas"()
    {
        when:
        String mensaje = service.registrarPrueba(MECANICA, '1', 100)
        then:
        mensaje == 'La prueba ' + MECANICA + ' ha sido registrada para el estudiante 1'

        when:
        mensaje = service.registrarPrueba(MECANICA, '2', 90)
        then:
        mensaje == 'La prueba ' + MECANICA + ' ha sido registrada para el estudiante 2'

        when:
        mensaje = service.registrarPrueba(MECANICA, '3', 80)
        then:
        mensaje == 'La prueba ' + MECANICA + ' ha sido registrada para el estudiante 3'

        when:
        mensaje = service.registrarPrueba(MECANICA, '4', 60)
        then:
        mensaje == 'La prueba ' + MECANICA + ' ha sido registrada para el estudiante 4'

        when:
        mensaje = service.registrarPrueba(MECANICA, '5', 35)
        then:
        mensaje == 'La prueba ' + MECANICA + ' ha sido registrada para el estudiante 5'

        when:
        mensaje = service.registrarPrueba(MECANICA, '6', 16)
        then:
        mensaje == 'La prueba ' + MECANICA + ' ha sido registrada para el estudiante 6'

        when:
        mensaje = service.registrarPrueba(MECANICA, '7', 99)
        then:
        mensaje == 'La prueba ' + MECANICA + ' ha sido registrada para el estudiante 7'

        when:
        mensaje = service.registrarPrueba(MECANICA, '8', 59)
        then:
        mensaje == 'La prueba ' + MECANICA + ' ha sido registrada para el estudiante 8'
    }

    void "test traer datos del estudiante después de pruebas mecánicas"()
    {
        when:
        User estudiante = service.traerDatosEstudiante('1')

        then:
        estudiante.puntos == 0
        estudiante.gemas == 1
        estudiante.estrellasSemanas[0] == 5
        estudiante.aporteSemanas[0] == 32

        when:
        estudiante = service.traerDatosEstudiante('2')
        then:
        estudiante.puntos == 0
        estudiante.gemas == 0
        estudiante.estrellasSemanas[0] == 4
        estudiante.aporteSemanas[0] == 16

        when:
        estudiante = service.traerDatosEstudiante('3')
        then:
        estudiante.puntos == 0
        estudiante.gemas == 0
        estudiante.estrellasSemanas[0] == 3
        estudiante.aporteSemanas[0] == 12

        when:
        estudiante = service.traerDatosEstudiante('4')
        then:
        estudiante.puntos == 0
        estudiante.gemas == 0
        estudiante.estrellasSemanas[0] == 2
        estudiante.aporteSemanas[0] == 9

        when:
        estudiante = service.traerDatosEstudiante('5')
        then:
        estudiante.puntos == 0
        estudiante.gemas == 0
        estudiante.estrellasSemanas[0] == 1
        estudiante.aporteSemanas[0] == 6

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
        estudiante.aporteSemanas[0] == 16

        when:
        estudiante = service.traerDatosEstudiante('8')
        then:
        estudiante.puntos == 0
        estudiante.gemas == 0
        estudiante.estrellasSemanas[0] == 1
        estudiante.aporteSemanas[0] == 6
    }

    void "test traer datos de la facción después de pruebas mecánicas"()
    {
        when:
        Faccion faccion = service.traerFaccion(FACCION)

        then:
        faccion.monedas == 31
        faccion.puntos == 0
        faccion.promedioMonedas() == 3
        faccion.promedioPuntos() == 0
    }

    void "test registrar pruebas cognitivas"()
    {
        when:
        String mensaje = service.registrarPrueba(COGNITIVA_FACIL, '1', 85)
        then:
        mensaje == 'La prueba ' + COGNITIVA_FACIL + ' ha sido registrada para el estudiante 1'

        when:
        mensaje = service.registrarPrueba(COGNITIVA_INTERMEDIA, '2', 99)
        then:
        mensaje == 'La prueba ' + COGNITIVA_INTERMEDIA + ' ha sido registrada para el estudiante 2'

        when:
        mensaje = service.registrarPrueba(COGNITIVA_DIFICIL, '3', 80)
        then:
        mensaje == 'La prueba ' + COGNITIVA_DIFICIL + ' ha sido registrada para el estudiante 3'

        when:
        mensaje = service.registrarPrueba(COGNITIVA_DIFICIL, '4', 0)
        then:
        mensaje == 'La prueba ' + COGNITIVA_DIFICIL + ' ha sido registrada para el estudiante 4'

        when:
        mensaje = service.registrarPrueba(COGNITIVA_DIFICIL, '5', 79)
        then:
        mensaje == 'La prueba ' + COGNITIVA_DIFICIL + ' ha sido registrada para el estudiante 5'

        when:
        mensaje = service.registrarPrueba(COGNITIVA_DIFICIL, '6', 100)
        then:
        mensaje == 'La prueba ' + COGNITIVA_DIFICIL + ' ha sido registrada para el estudiante 6'

        when:
        mensaje = shouldFail{service.registrarPrueba(COGNITIVA_DIFICIL, '6', 99)}
        then:
        mensaje == 'Hubo un problema al registrar la prueba ' + COGNITIVA_DIFICIL + ' para el estudiante 6: This mission is not available for this player'
    }

    void "test traer datos del estudiante después de pruebas cognitivas"()
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
        estudiante.puntos == 20
        estudiante.gemas == 3
        estudiante.medallas == 0
        estudiante.estrellasSemanas[0] == 0
    }

    void "test traer datos de la facción después de pruebas cognitivas"()
    {
        when:
        Faccion faccion = service.traerFaccion(FACCION)

        then:
        faccion.monedas == 31
        faccion.puntos == 51
        faccion.promedioMonedas() == 3
        faccion.promedioPuntos() == 6
    }

    void "test registrar pruebas honoríficas"()
    {
        when:
        String mensaje = service.registrarPrueba(HONORIFICA, '3', 80)
        then:
        mensaje == 'La prueba ' + HONORIFICA + ' ha sido registrada para el estudiante 3'

        when:
        mensaje = service.registrarPrueba(HONORIFICA, '4', 80)
        then:
        mensaje == 'La prueba ' + HONORIFICA + ' ha sido registrada para el estudiante 4'

        when:
        mensaje = service.registrarPrueba(HONORIFICA, '6', 79)
        then:
        mensaje == 'La prueba ' + HONORIFICA + ' ha sido registrada para el estudiante 6'

        when:
        mensaje = shouldFail{service.registrarPrueba(HONORIFICA, '6', 80)}
        then:
        mensaje == 'Hubo un problema al registrar la prueba ' + HONORIFICA + ' para el estudiante 6: This mission is not available for this player'
    }

    void "test traer datos del estudiante después de pruebas honorificas"()
    {
        when:
        User estudiante = service.traerDatosEstudiante('3')
        then:
        estudiante.puntos == 50
        estudiante.gemas == 3
        estudiante.medallas == 1
        estudiante.estrellasSemanas[0] == 3

        when:
        estudiante = service.traerDatosEstudiante('6')
        then:
        estudiante.puntos == 20
        estudiante.gemas == 3
        estudiante.medallas == 0
        estudiante.estrellasSemanas[0] == 0
    }

    void "test traer datos de la facción después de pruebas honoríficas"()
    {
        when:
        Faccion faccion = service.traerFaccion(FACCION)

        then:
        faccion.monedas == 31
        faccion.puntos == 111
        faccion.promedioMonedas() == 3
        faccion.promedioPuntos() == 13
    }

    void "test traer comparativo de facciones"()
    {
        when:
        Seccion seccion = service.traerSeccion(FACCION)

        then:
        seccion.facciones[0].promedioMonedas() == 3
        seccion.facciones[0].promedioPuntos() == 13
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