<!doctype html>
<html lang="es">
	<head>
		<meta name="layout" content="mainBootstrap"/>
	</head>
<body>

<section>

		<g:form url="[action:'stAssesmentCourseGrades']" method="POST" >
		
				<strong>Actividad: </strong>
				<g:select from="${users}" name="username" optionValue="${{it.nombre +' '+it.faccion.nombreFaccion}}" optionKey="username" noSelection="${['Empty':'Elegir uno']}" />
				<g:actionSubmit class="btn btn-green" action="dashboardEstudiante" value="Enviar" />
				<br />
				<br />
		</g:form>

<ul class="nav nav-tabs">
  <li class="active"><a data-toggle="tab" href="#individual">Individual</a></li>
  <li><a data-toggle="tab" href="#faccion">Facción</a></li>
</ul>

<div class="tab-content">
  <div id="individual" class="tab-pane fade in active">
   <div class="container">
    <div class="col-xs-12 col-sm-4">
      <div class="boxSem">
		<br />
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
		<br />
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

</body>
</html>
