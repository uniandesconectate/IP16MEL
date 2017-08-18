html>
	<head>
		<meta name="layout" content="mainBootstrap"/>
	</head>
	<body>
		${message}
        <h3 style="margin-left:20px;">Cargar juego</h3>
		<div style="margin-left:20px;" id="formulario">
			<g:uploadForm name="form" action="cargarInformacion">
                <label for="archivo">Archivo a cargar</label>
				<div class="form-group">
					<input style="float:left;margin-right:10px;" id="archivo" type="file" name="archivo" class="btn btn-default"/>
                    <input type="submit" value="Subir archivo" class="btn btn-default"/>
                </div>
			</g:uploadForm>
		</div>
		<div id="carga" style="display: none;">
			<asset:image src="spinner.gif"/>
		</div>
	</body>
</html>