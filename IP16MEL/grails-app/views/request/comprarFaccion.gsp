<!doctype html>
<html lang="es">
<html lang="es">
	<head>
		<meta name="layout" content="mainBootstrap"/>
	</head>
	<body>
		<section>
			<div class="container">
				<h1>Compra de poderes</h1>
				<g:form url="[action:'comprarFaccionSave']" method="POST" >
					<strong>Elija la facción: </strong>
					<g:select from="${facciones}" name="faccionId" id="faccionId"  optionKey="nombreFaccion" optionValue="${{it.nombreFaccion + ' (' + it.monedas + ' monedas)'}}"/>
					<br />
					<strong>Cantidad de monedas: </strong>
					<g:field name="value1" maxlength="10" required="true" type="number" value="0" min="1" />
					<br />
					<g:actionSubmit class="btn btn-green" action="comprarFaccionSave" value="Enviar" />
					<br />
				</g:form>
			</div>
		</section>

	</body>
</html>
