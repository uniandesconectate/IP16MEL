html>
    <head>
        <meta name="layout" content="mainBootstrap"/>
    </head>
    <body>
    ${message}
    <h3>Cargar estudiantes</h3>
    <div id="formulario">
        <g:uploadForm name="form" action="cargarEstudiantes">
            <div class="form-group">
                <label for="archivoID">Archivo a cargar</label>
                <input type="file" name="archivo" class="btn btn-default"/>
            </div>
            <br />

            <input type="submit" value="Subir archivo" class="btn btn-default"/>
        </g:uploadForm>
    </div>
    <div id="carga" style="display: none;">
        <asset:image src="spinner.gif"/>
    </div>
    </body>
</html>