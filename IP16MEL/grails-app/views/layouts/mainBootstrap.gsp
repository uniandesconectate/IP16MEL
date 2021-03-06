<!doctype html>
<html lang="es">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>Matématica Estructural y Lógica | Universidad de Los Andes</title>
		<link rel="shortcut icon" href="favicon.ico" />
		<asset:stylesheet src="font-awesome.min.css" />
		<asset:stylesheet src="custom.min.css" />
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
		<g:layoutHead/>
	</head>
	<body>
		<header>
			<div class="container">
				<div class="col-xs-12 col-md-8">
					<h1><g:link uri="/">Matématica Estructural y Lógica</g:link></h1>
				</div>
				<sec:ifLoggedIn>
					<div class="col-xs-12 col-md-4">
						<div class="userBox">
							<div class="user">${userName}</div>
							<g:link controller="logout">Salir</g:link>
						</div>
					</div>
				</sec:ifLoggedIn>
				<sec:ifNotLoggedIn>
					<div class="col-xs-12 col-md-4">
						<div class="userBox">
							<div class="user"></div>
							<g:link controller="request" action="index">Login</g:link><br />
						</div>
					</div>
				</sec:ifNotLoggedIn>
			</div>
		</header>

		<g:layoutBody/>

		<footer>
			<div class="container">
				<div class="col-xs-12 col-sm-3">
					<a href="http://uniandes.edu.co/" target="_blank">
						<asset:image src="uniandes.png" alt="Universidad de Los Andes" />
					</a>
				</div>
				<div class="col-xs-12 col-sm-6">
					Universidad de los Andes | Vigilada Mineducación<br />
					Reconocimiento como Universidad: Decreto 1297 del 30 de mayo de 1964.<br />
					Reconocimiento personería jurídica: Resolución 28 del 23 de febrero de 1949 Minjusticia.<br />
					Cra 1 Nº 18A- 12 Bogotá, (Colombia) | Código postal: 111711 | Tels: +571 3394949 - +571 3394999
				</div>
				<div class="col-xs-12 col-sm-3">
					<a href="http://conectate.uniandes.edu.co/" target="_blank">
					<asset:image src="conectate.png" alt="Conecta-TE" />
					</a>
				</div>
			</div>
		</footer>
	
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	
	</body>
</html>