<!doctype html>
<html lang="es">
	<head>
		<meta name="layout" content="mainBootstrap"/>
	</head>
	<body>
		<section>
			<div class="container">
				<h1>Compra de ayudas</h1>
				<g:form url="[action:'comprarEnGrupoSave']" method="POST" >
					<strong>Elija el primer estudiante: </strong>
					<g:select from="${users}" name="userId1" id="userId1"  optionKey="id" optionValue="nombre"/>
					<br />
					<strong>Cantidad de gemas: </strong>
					<g:field name="value1" maxlength="10" required="true" type="number" value="0" min="0" />
					<br />
					<hr />
					<strong>Elija el segundo estudiante: </strong>
					<g:select from="${users}" name="userId2" id="userId2"  optionKey="id" optionValue="nombre"/>
					<br />
					<strong>Cantidad de gemas: </strong>
					<g:field name="value2" maxlength="10" required="true" type="number" value="0" min="0" />
					<br />
					<hr />
					<g:actionSubmit class="btn btn-green" action="comprarEnGrupoSave" value="Enviar" />
					<br />
				</g:form>
			</div>
		</section>
	</body>
</html>
