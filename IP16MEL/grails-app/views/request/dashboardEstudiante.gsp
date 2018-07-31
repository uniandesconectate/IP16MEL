<!doctype html>
<html lang="es">
	<head>
		<meta name="layout" content="mainBootstrap"/>
	</head>
<body>

<section>

		<g:form url="[action:'stAssesmentCourseGrades']" method="POST" style="margin-left:1%;" >
				<strong>Actividad: </strong>
				<g:select from="${estudiantes.sort{it.user.username}}" name="username" optionValue="${{it.nombre + ' ('+it.equipo.nombre + ')'}}" optionKey="${{it.user.username}}" noSelection="${['Empty':'Elegir uno']}" />
				<g:actionSubmit class="btn btn-green" action="dashboardEstudiante" value="Enviar" />
				<br />
				<br />
		</g:form>

<ul class="nav nav-tabs" style="margin-left:1%;">
  <li class="active"><a data-toggle="tab" href="#equipo">Equipo</a></li>
  <li><a data-toggle="tab" href="#individual">Individual</a></li>

</ul>

<div class="tab-content">
  <div id="individual" class="tab-pane fade">
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
                Monedas ganadas por semana
              </th>
              <th>
                Aporte a mi equipo por semana
              </th>
            </tr>
          </thead>
          <tbody>
          	<g:each in="${semanas}" var="semana" status="i">
	            <!-- item -->
	            <tr>
	              <td>${semana}</td>
	              <td>
                      <span class="mon"> X ${monedas[i]}</span>
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
                <span class="gem">Gemas ${estudiante.gemas}</span>
                  <span class="cop">Puntos ${estudiante.puntos}</span>
              </td>
              <td>
                  <span class="med">Medallas ${estudiante.medallas}</span>
                  <span class="mon">Monedas ${estudiante.monedas}</span>
              </td>
            </tr>
          </tfoot>
        </table>
        <!--  TABLA SEMANA -->
      </div>
    </div>
  </div>  
 </div>


    
<div id="equipo" class="tab-pane fade in active">
<br />
  <div class="container">
    <div class="col-xs-12 col-sm-8">
      <div class="boxFacciones">
          <br />
          <ul class="nav nav-tabs">
            <g:each in="${equipos.sort{it.nombre.substring(it.nombre.lastIndexOf(' ')).toInteger()}}" status="i" var="equipo">
                <g:if test="${i == 0}">
                    <li class="active"><a data-toggle="tab" href="#equipo${i+1}">Equipo ${equipo.nombre.substring(equipo.nombre.lastIndexOf(' '))}</a></li>
                </g:if>
                <g:else>
                    <li><a data-toggle="tab" href="#equipo${i+1}">Equipo ${equipo.nombre.substring(equipo.nombre.lastIndexOf(' '))}</a></li>
                </g:else>
                <g:set var="cnt" value="${equipo.nombre.substring(equipo.nombre.lastIndexOf(' ')).toInteger()}" />
            </g:each>
          </ul>
          <div class="tab-content">
              <g:each in="${equipos.sort{it.nombre.substring(it.nombre.lastIndexOf(' ')).toInteger()}}" status="i" var="equipo">
                  <g:if test="${i == 0}">
                      <div id="equipo${i+1}" class="tab-pane fade in active">
                  </g:if>
                  <g:else>
                      <div id="equipo${i+1}" class="tab-pane fade">
                  </g:else>
                      <div class="fac1">
                          <g:form action="cambiarEquipos" method="POST">
                              <input type="hidden" name="numMiembros" value="${equipo.miembros.size()}" />
                              <input type="hidden" name="equipoAc" value="${equipo.nombre}" />
                              <input type="hidden" name="cnt" value="${cnt + 1}" />
                              <table width="100%" cellpadding="0" cellspacing="0">
                                  <thead>
                                  <tr>
                                      <th colspan="5">
                                          <i class="fa fa-users" aria-hidden="true"></i> ${equipo.nombre}
                                      </th>
                                  </tr>
                                  <tr>
                                      <th>
                                          <span class="login"><i class="fa fa-user-o" aria-hidden="true"></i> Login</span>
                                      </th>
                                      <th>
                                          <span class="cop"> Puntos X ${equipo.puntos} - Promedio ${equipo.promedioPuntos()}</span>
                                      </th>
                                      <th>
                                          <span class="mon"> Monedas X ${monedasTotal[i]} - Disponibles ${equipo.monedas}</span>
                                      </th>
                                      <th>
                                          <span class="med">Medallas</span>
                                      </th>
                                      <th style="text-align:center;background: #82bc79">
                                          <button type="submit" style="padding: 1% 4% 1% 4%;" class="btn btn-danger btn-sm" data-toggle="tooltip" title="Actualizar equipos"><span class="glyphicon glyphicon-refresh"></span></button> <span style="font-weight:400;">Equipo</span>
                                      </th>
                                  </tr>
                                  </thead>
                                  <tbody>
                                  <g:each in="${equipo.miembros.sort(false){-it.puntos}}" var="miembro" status="k">
                                      <tr>
                                          <td>${miembro.user.username}</td>
                                          <td style="text-align:center;">${miembro.puntos}</td>
                                          <td style="text-align:center;">${miembro.monedas}</td>
                                          <td>
                                              <g:set var="counter" value="${0}"/>
                                              <g:while test="${counter < miembro.medallas}">
                                                  <asset:image src="med.png" alt="Medalla" />
                                                  <g:set var="counter" value="${counter+1}"/>
                                              </g:while>
                                          </td>
                                          <td>
                                              <input type="hidden" name="miembro_${k}" value="${miembro.user.username}" />
                                              <select name="selEquipo_${k}" style="margin:5% 0% 5% 0%;">
                                                  <option value="igual" selected>--</option>
                                                    <g:each in="${equipos.sort{it.nombre.substring(it.nombre.lastIndexOf(' ')).toInteger()}}" status="e2" var="equipo2">
                                                        <g:if test="${equipo != equipo2}">
                                                            <option value="${equipo2.nombre}">${equipo2.nombre.substring(equipo2.nombre.length() - 8, equipo2.nombre.length())}</option>
                                                        </g:if>
                                                    </g:each>
                                                  <option value="nuevo">Equipo ${cnt + 1}</option>
                                              </select>
                                          </td>
                                      </tr>
                                  </g:each>
                                  </tbody>
                              </table>
                          </g:form>
                      </div>
                  </div>
              </g:each>
          </div>
          </div>
      </div>
    </div>
  </div>
 </div>
</section>

</body>
</html>
