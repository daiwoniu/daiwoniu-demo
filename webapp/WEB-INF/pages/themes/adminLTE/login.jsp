<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Demo系统登录</title>
    <link href="/assets/themes/adminLTE/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="/assets/themes/adminLTE/adminLTE/css/AdminLTE.min.css" rel="stylesheet">
    <link href="/assets/font-awesome/css/font-awesome.min.css" rel="stylesheet">


    <link href="/assets/jquery.uniform/themes/default/css/uniform.default.min.css" rel="stylesheet">
    <link href="/assets/app/application.css" rel="stylesheet">
    
    <!--[if lt IE 9]><!--<script src="/assets/html5shiv/html5shiv.js"></script>--><![endif]-->
    <script src="/assets/themes/adminLTE/jquery/jquery.min.js"></script>
    <%--<script src="/assets/jquery-migrate/jquery-migrate.min.js"></script>--%>
    <script src="/assets/jquery.validation/dist/jquery.validate.min.js"></script>
    <script src="/assets/jquery.validation/src/localization/messages_zh.js"></script>
    <script src="/assets/jquery.uniform/jquery.uniform.min.js"></script>

    <script type="text/javascript">
        $(function() {
            $("input:checkbox, input:radio, input:file").not(
                    '[data-no-uniform="true"],#uniform-is-ajax').uniform();
            $("form.login-form").validate({
                errorElement : "span",
                success : function($label) {
                    $label.addClass("valid").text(" ");
                },
                onfocusout : function(element) {
                    $(element).valid();
                }
            });
        });
        function refreshCode(imgObj){
            if (!imgObj) {
                imgObj = document.getElementById("validationCodeImg");
            }
            var index = imgObj.src.indexOf("?");
            if(index != -1) {
                var url = imgObj.src.substring(0,index + 1);
                imgObj.src = url + Math.random();
            } else {
                imgObj.src = imgObj.src + "?" + Math.random();
            }
        }
    </script>
</head>
<body class="login-page">
<div class="login-box">
    <div class="login-logo">
        <b>Demo系统</b>
    </div><!-- /.login-logo -->
    <div class="login-box-body">
        <div class="box-header">
            <h3 class="box-title">登录</h3>
        </div><!-- /.box-header -->
        <div class="box-body">
            <div class="form-group">
                <c:if test="${errorMsg != null}">
                    <div class="alert-danger">${errorMsg}</div>
                </c:if>
            </div>
            <form:form class="login-form" action="/admin/login" method="post" modelAttribute="loginForm">
                <div class="control-group has-feedback">
                    <div class="controls">
                        <form:input path="name" class="required form-control" placeholder="登录名" />
                        <span class="glyphicon glyphicon-user form-control-feedback"></span>
                    </div>
                </div>
                <div class="control-group has-feedback">
                    <div class="controls">
                        <form:password path="password" class="required form-control" placeholder="密码" />
                        <span class="glyphicon glyphicon-lock form-control-feedback"></span>
                    </div>
                </div>
                <div class="control-group has-feedback">
                    <div class="controls" style="width: 200px;">
                        <form:input path="code" class="required form-control" placeholder="验证码" />
                        <span style="position:absolute;top: 0;right: 0;width: auto;"><img id="validationCodeImg" src="/common/codeimg" alt="验证码" onclick="refreshCode(this);"/></span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-4">
                        <button type="submit" class="btn btn-primary btn-block btn-flat">登录</button>
                    </div><!-- /.col -->

<%--                    <div class="col-xs-8 text-right">
                       <a href="/sessions/register">注册新用户</a>
                    </div>--%>
                </div>
            </form:form>
        </div>
    </div><!-- /.login-box-body -->
</div><!-- /.login-box -->
</body>
</html>
