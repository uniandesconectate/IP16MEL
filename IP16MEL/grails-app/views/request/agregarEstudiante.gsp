<!doctype html>
<html lang="es">
    <head>
        <meta name="layout" content="mainBootstrap"/>
    </head>
    <body>
        <section>
            <div class="container">
                <h1>Agregar estudiante</h1>
                <g:form url="[action:'agregarEstudianteSave']" method="POST" >
                    <strong>Equipo al cual se agregará el estudiante: </strong>
                    <g:select from="${equipos}" name="equipoId" id="equipoId"  optionKey="nombre" optionValue="${{it.nombre}}"/>
                    <br />
                    <strong>Login del nuevo estudiante: </strong>
                    <g:field name="login" required="true" type="text" />
                    <br />
                    <g:actionSubmit class="btn btn-green" action="agregarEstudianteSave" value="Enviar" />
                    <br />
                </g:form>
            </div>
        </section>
    </body>
</html>