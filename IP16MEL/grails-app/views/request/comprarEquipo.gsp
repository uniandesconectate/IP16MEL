<!doctype html>
<html lang="es">
	<head>
		<meta name="layout" content="mainBootstrap"/>
	</head>
	<body>
		<section>
			<div class="container">
				<h1>Compra de poderes</h1>
				<g:form url="[action:'comprarEquipoSave']" method="POST" >
					<strong>Elija el equipo: </strong>
					<g:select from="${equipos}" name="equipoId" id="equipoId"  optionKey="nombre" optionValue="${{it.nombre + ' (' + it.monedas + ' monedas)'}}"/>
					<br />
					<strong>Cantidad de monedas: </strong>
					<g:field name="value1" maxlength="10" required="true" type="number" value="0" min="1" />
					<br />
					<g:actionSubmit class="btn btn-green" action="comprarEquipoSave" value="Enviar" />
					<br />
				</g:form>
			</div>
		</section>
	</body>
</html>