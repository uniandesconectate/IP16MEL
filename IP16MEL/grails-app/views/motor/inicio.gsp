<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="mainBootstrap"/>
        <script>
            $(document).ready(function()
            {
                setTimeout(function(){ $('#alertBox').remove();}, 5000);
            });
        </script>
    </head>
    <body>
        <g:if test="${flash.message}">
            <div id="alertBox" style="width:60%;position:fixed;top:15%;left:20%;z-index:1;" class="alert alert-info alert-dismissable" role="alert">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">X</a>
                <strong>${flash.message}</strong>
            </div>
        </g:if>
        <g:if test="${flash.error}">
            <div id="alertBox" style="width:60%;position:fixed;top:15%;left:20%;z-index:1;" class="alert alert-danger alert-dismissable" role="alert">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">X</a>
                <strong>${flash.error}</strong>
            </div>
        </g:if>
        <p>${json}</p>
        <g:link controller="motor" action="invocarMotor">Invocar motor</g:link><br />
    </body>
</html>