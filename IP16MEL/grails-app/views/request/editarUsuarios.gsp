html>
    <head>
        <meta name="layout" content="mainBootstrap"/>
    </head>
    <body>
    ${message}
    <h3 style="margin-left:20px;">Cargar usuarios</h3>
    <div style="margin-left:20px;" id="formulario">
        <g:uploadForm name="form" action="cargarEstudiantes">
            <label for="archivo">Archivo de estudiantes a cargar</label>
            <div class="form-group">
                <input style="float:left;margin-right:10px;" id="archivo" type="file" name="archivo" class="btn btn-default"/>
                <input type="submit" value="Subir archivo" class="btn btn-default"/>
            </div>
        </g:uploadForm>
        <br/>
        <g:uploadForm name="form" action="cargarProfesores">
            <label for="archivo2">Archivo de profesores a cargar</label>
            <div class="form-group">
                <input style="float:left;margin-right:10px;" id="archivo2" type="file" name="archivo" class="btn btn-default"/>
                <input type="submit" value="Subir archivo" class="btn btn-default"/>
            </div>
        </g:uploadForm>
    </div>
    <div id="carga" style="display: none;">
        <asset:image src="spinner.gif"/>
    </div>
    </body>
</html>