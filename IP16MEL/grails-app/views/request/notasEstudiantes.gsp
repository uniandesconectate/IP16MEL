<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="mainBootstrap"/>
    </head>
    <body>
        <section>
        <div class="container">
            <h2>Notas de los estudiantes en escala [0..100]</h2>
            <g:form>
                <g:actionSubmit class="btn btn-green" action="notasSave" value="Guardar" />
                <g:each in="${equipos.sort{it.nombre}}" var="eq" status="e">
                    <h3>${eq.nombre}</h3>
                    <div style="overflow: auto;">
                        <table>
                            <tr>
                                <th></th>
                                <g:each in="${pruebas}" var="pr" status="p">
                                    <th>${pr}</th>
                                </g:each>
                                <th></th>
                            </tr>
                            <g:each in="${eq.miembros.sort{it.user.username}}" var="mi" status="m">
                                <tr>
                                    <th>${mi.user.username}</th>
                                    <g:each in="${(0..pruebas.size() - 1)}" var="pr" status="p">
                                        <td><input min="0" max="100" type="number" value="${mi.notas[pr] > 0 ? mi.notas[pr] : ''}" name="pru_${mi.id}_${pr}" style="width:100px;" placeholder="${mi.user.username}" ${mi.motor[pr] ? 'disabled' : ''} /></td>
                                    </g:each>
                                    <th>${mi.user.username}</th>
                                </tr>
                            </g:each>
                        </table>
                    </div>
                </g:each>
            </g:form>
        </div>
        </section>
    </body>
</html>