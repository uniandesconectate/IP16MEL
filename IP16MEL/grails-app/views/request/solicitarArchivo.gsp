html>
	<head>
		<meta name="layout" content="mainBootstrap"/>
	</head>
	<body>
		${message}
		<h3>Subir información</h3>
		<div id="formulario">
			<g:uploadForm name="form" action="cargarInformacion">
				<div class="form-group">
			  		<label for="archivoID">Archivo a cargar</label>
					<input type="file" name="archivo" class="btn btn-default"/>
				</div>
				<br />
				<div class="form-group">
			  		<label for="semanaId">Semana</label><br />
					<g:field type="Number" name="semana" required="true" min="1" value="1" />
				</div>
				<div class="form-group">
			  		<label for="separadorId">Separador</label><br />
					<g:field type="Text" name="separador" required="true" value="|" />
				</div>
				
				<input type="submit" value="Subir archivo" class="btn btn-default"/> 
			</g:uploadForm>
		</div>
		<div id="carga" style="display: none;">
			<asset:image src="spinner.gif"/>
		</div>
	</body>
</html>