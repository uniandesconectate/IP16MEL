<!doctype html>
<html lang="es">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="author" content="David Vargas | Uniandes">
  <meta name="description" content="">
  <title>Matématica Estructural y Lógica | Universidad de Los Andes</title>
  <link rel="shortcut icon" href="favicon.ico" />
  <asset:stylesheet src="font-awesome.min.css" />
  <asset:stylesheet src="custom.min.css" />
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<header>
  <div class="container">
    <div class="col-xs-12 col-md-8">
      <h1>Matématica Estructural y Lógica</h1>
    </div>
    <div class="col-xs-12 col-md-4">
      <div class="userBox">
        <div class="user">${userName}</div>
        <g:link controller="logout">Salir</g:link>
      </div>
    </div>
  </div>
</header>

<section>
<ul class="nav nav-tabs">
  <li class="active"><a data-toggle="tab" href="#individual">Individual</a></li>
  <li><a data-toggle="tab" href="#faccion">Facción</a></li>
</ul>

<div class="tab-content">
  <div id="individual" class="tab-pane fade in active">
   <div class="container">
    <div class="col-xs-12 col-sm-4">
      <div class="boxSem">
        <!--  TABLA SEMANA -->
        <table width="100%" cellpadding="0" cellspacing="0">
          <thead>
            <tr>
              <th>
                <i class="fa fa-calendar" aria-hidden="true"></i>
              </th>
              <th>
                Estrellas ganadas por semana
              </th>
              <th>
                Aporte a mi facción por semana
              </th>
            </tr>
          </thead>
          <tbody>
          	<g:each in="${semanas}" var="semana" status="i">
	            <!-- item -->
	            <tr>
	              <td>${semana}</td>
	              <td>
	                <i class="fa fa-star" aria-hidden="true"></i> X ${estrellas[i]}
	              </td>
	              <td>
	                <div class="progress">
	                  <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="${porcentajes[i]}" aria-valuemin="0" aria-valuemax="100" style="width: ${porcentajes[i]}%">
	                    <span class="sr-only">${porcentajes[i]}%</span>
	                  </div>
	                </div>
	              </td>
	            </tr>
	            <!-- item -->
            </g:each>
          </tbody>
          <tfoot>
            <tr>
              <td colspan="2">
                <span class="gem">${gemas} - Puntos: ${user.puntos}</span>
              </td>
              <td>
                <span class="med">${medallas}</span>
              </td>
            </tr>
          </tfoot>
        </table>
        <!--  TABLA SEMANA -->
      </div>
    </div>
  </div>  
 </div>


    
<div id="faccion" class="tab-pane fade">
<br />
  <div class="container">
    <div class="col-xs-12 col-sm-8">
      <div class="boxFacciones">
        <div class="fac1">
          <table width="100%" cellpadding="0" cellspacing="0">
            <thead>
              <tr>
                <th colspan="3">
                  <i class="fa fa-users" aria-hidden="true"></i> Facción 1
                </th>
              </tr>
              <tr>
                <th>
                  <span class="cop"> X ${faccion1Copas}</span>
                </th>
                <th colspan="2">
                  <span class="mon"> X ${faccion1Monedas}</span>
                </th>
              </tr>
            </thead>
            <tbody>
            	<g:each in="${faccion1Nombres}" var="faccionNombre" status="i">
	              <tr>
	                <td>${faccionNombre}</td>
	                <td>
		            	<g:set var="counter" value="${0}"/>
		            	<g:while test="${counter < faccion1Medallas[i]}">
	                  		<asset:image src="med.png" alt="Medalla" />
	                  		<g:set var="counter" value="${counter+1}"/>
	                  	</g:while>
	                </td>
	                <td>
	                  ${faccion1Puntos[i]}
	                </td>
	              </tr>
	            </g:each>
            </tbody>
          </table>
        </div>
        <div class="fac2">
          <table width="100%" cellpadding="0" cellspacing="0">
            <thead>
              <tr>
                <th colspan="3">
                  <i class="fa fa-users" aria-hidden="true"></i> Facción 2
                </th>
              </tr>
              <tr>
                <th>
                  <span class="cop"> X ${faccion2Copas}</span>
                </th>
                <th colspan="2">
                  <span class="mon"> X ${faccion2Monedas}</span>
                </th>
              </tr>
            </thead>
            <tbody>
            	<g:each in="${faccion2Nombres}" var="faccionNombre" status="i">
	              <tr>
	                <td>${faccionNombre}</td>
	                <td>
		            	<g:set var="counter" value="${0}"/>
		            	<g:while test="${counter < faccion2Medallas[i]}">
	                  		<asset:image src="med.png" alt="Medalla" />
	                  		<g:set var="counter" value="${counter+1}"/>
	                  	</g:while>
	                </td>
	                <td>
	                  ${faccion2Puntos[i]}
	                </td>
	              </tr>
	            </g:each>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
 </div>
</section>


<footer>
  <div class="container">
    <div class="col-xs-12 col-sm-3">
      <a href="http://uniandes.edu.co/" target="_blank">
        <asset:image src="uniandes.png" alt="Universidad de Los Andes" />
      </a>
    </div>
    <div class="col-xs-12 col-sm-6">
      Universidad de los Andes | Vigilada Mineducación
      <br />
      Reconocimiento como Universidad: Decreto 1297 del 30 de mayo de 1964.
      <br />
      Reconocimiento personería jurídica: Resolución 28 del 23 de febrero de 1949 Minjusticia.
      <br />
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
