<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="mainBootstrap"/>
    </head>
    <body>
        <g:form>
            <g:actionSubmit style="margin:3% 1% 1% 3%;" class="btn btn-green" action="notasSave" value="Guardar" />
            <g:each in="${equipos.sort{it.nombre}}" var="eq" status="e">
                <h2 style="margin-left:3%;">${eq.nombre}</h2>
                <div style="margin-left:5%;width:90%;overflow: auto;">
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
                                    <td><input type="number" value="${mi.notas[pr] > 0 ? mi.notas[pr] : ''}" name="pru_${mi.id}_${pr}" style="width:100px;" placeholder="${mi.user.username}" /></td>
                                </g:each>
                                <th>${mi.user.username}</th>
                            </tr>
                        </g:each>
                    </table>
                </div>
            </g:each>
        </g:form>
    </body>
</html>