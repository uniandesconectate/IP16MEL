<!doctype html>
<html lang="es">
    <head>
        <meta name="layout" content="mainBootstrap"/>
    </head>
    <body>
        <section>
            <div class="container">
                <h1>Reinicio de monedas</h1>
                <g:form url="[action:'reiniciarMonedasSave']" method="POST" >
                    <strong>Elija la sección: </strong>
                    <g:select from="${secciones}" name="nombreSeccion" id="nombreSeccion" optionKey="nombre" optionValue="${{it.nombre}}"/>
                    <br />
                    <g:actionSubmit class="btn btn-green" action="reiniciarMonedasSave" value="Enviar" />
                    <br />
                </g:form>
            </div>
        </section>
    </body>
</html>