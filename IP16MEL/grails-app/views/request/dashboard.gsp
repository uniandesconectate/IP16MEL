<!doctype html>
<html lang="es">
    <head>
        <meta name="layout" content="mainBootstrap"/>
    </head>
    <body>
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
                                <br />
                                <!--  TABLA QUINCENA -->
                                <table width="100%" cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th><i class="fa fa-calendar" aria-hidden="true"></i></th>
                                            <th>Estrellas ganadas por quincena</th>
                                            <th>Aporte a mi facción por quincena</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <g:each in="${quincenas}" var="quincena" status="i">
                                        <!-- item -->
                                        <tr>
                                            <td>${quincena}</td>
                                            <td><i class="fa fa-star" aria-hidden="true"></i> X ${estrellas[i]}</td>
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
                                            <td colspan="2"><span class="gem">${user.gemas} - Puntos: ${user.puntos}</span></td>
                                            <td><span class="med">${user.medallas}</span></td>
                                        </tr>
                                    </tfoot>
                                </table>
                                <!--  TABLA QUINCENA -->
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
                                <ul class="nav nav-tabs">
                                    <li class="active"><a data-toggle="tab" href="#faccion1">A</a></li>
                                    <li><a data-toggle="tab" href="#faccion2">B</a></li>
                                    <li><a data-toggle="tab" href="#faccion3">C</a></li>
                                    <li><a data-toggle="tab" href="#faccion4">D</a></li>
                                </ul>
                                <div class="tab-content">
                                    <g:each in="${facciones}" status="i" var="faccion">
                                        <g:if test="${i == 0}">
                                        <div id="faccion${i+1}" class="tab-pane fade in active">
                                        </g:if>
                                        <g:else>
                                        <div id="faccion${i+1}" class="tab-pane fade">
                                        </g:else>
                                            <div class="fac1">
                                                <table width="100%" cellpadding="0" cellspacing="0">
                                                    <thead>
                                                        <tr>
                                                            <th colspan="3"><i class="fa fa-users" aria-hidden="true"></i> ${faccion.nombreFaccion}</th>
                                                        </tr>
                                                        <tr>
                                                            <th><span class="cop"> X ${faccion.puntos} - Promedio ${faccion.promedioPuntos()}</span></th>
                                                            <th colspan="2"><span class="mon"> X ${faccion.monedas} - Prom ${faccion.promedioMonedas()}</span></th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <g:each in="${faccion.miembros.sort(false){-it.puntos}}" var="miembro" status="k">
                                                            <tr>
                                                                <td>${miembro.username}</td>
                                                                <td>
                                                                    <g:set var="counter" value="${0}"/>
                                                                    <g:while test="${counter < miembro.medallas}">
                                                                        <asset:image src="med.png" alt="Medalla" />
                                                                        <g:set var="counter" value="${counter+1}"/>
                                                                    </g:while>
                                                                </td>
                                                                <td>${miembro.puntos}</td>
                                                            </tr>
                                                        </g:each>
                                                    </tbody>
                                                </table>
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