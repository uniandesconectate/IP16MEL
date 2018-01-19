<!doctype html>
<html lang="es">
	<head>
		<meta name="layout" content="mainBootstrap"/>
		<script>
			function selEstudiante()
            {
                $('#userId2').val($('#userId1').val());
            }
		</script>
	</head>
	<body>
		<section>
			<div class="container">
				<h1>Compra de ayudas</h1>
				<g:form url="[action:'comprarEnGrupoSave']" method="POST" >
					<strong>Estudiante 1: </strong>
					<g:select onchange="selEstudiante()" from="${estudiantes}" name="userId1" id="userId1"  optionKey="${{it.user.username}}" optionValue="${{it.user.username + ': ' + it.gemas + ' gemas' + ' ('+it.equipo.nombre + ')'}}"/>
					<br />
					<strong>Cantidad de gemas: </strong>
					<g:field name="value1" maxlength="10" required="true" type="number" value="0" min="0" />
					<br />
					<hr />
					<strong>Estudiante 2: </strong>
					<g:select from="${estudiantes}" name="userId2" id="userId2"  optionKey="${{it.user.username}}" optionValue="${{it.user.username + ': ' + it.gemas + ' gemas' + ' ('+it.equipo.nombre + ')'}}"/>
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