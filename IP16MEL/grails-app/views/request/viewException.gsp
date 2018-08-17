<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="main"/>
        <title></title>
    </head>
    <body>
        <a href="#" onclick="window.history.back();"><button style="margin: 1% 0% 0% 10%;" type="button" data-toggle="tooltip" title="Atrás" class="btn-sm btn-warning"><i class="fa fa-arrow-left"></i></button></a>
        <div style="margin: 2% 0% 0% 10%;width:80%;">
            <h3>Excepción</h3>
            <br>
            <h5>${exception.getMessage()}</h5>
            <br>
        </div>
    </body>
</html>