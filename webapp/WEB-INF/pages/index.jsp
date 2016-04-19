<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="org.joda.time.DateTime" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<title>Demo</title>
	<link href="/assets/themes/adminLTE/adminLTE/css/AdminLTE.min.css" rel="stylesheet">
	<link href="/assets/themes/adminLTE/adminLTE/css/skins/_all-skins.min.css" rel="stylesheet">
	<link href="/assets/themes/adminLTE/bootstrap/css/bootstrap.min.css" rel="stylesheet">

	<link href="/assets/common.css" rel="stylesheet">

	<!--[if lt IE 9]><!--<script src="/assets/html5shiv/dist/html5shiv.js"></script>--><![endif]-->

	<script src="/assets/themes/adminLTE/jquery/jquery.min.js"></script>
</head>
<body class="skin-blue layout-top-nav">
	<div class="wrapper">

		<!-- Main Header -->
		<header class="main-header">
			<!-- Header Navbar -->
			<nav class="navbar navbar-static-top">
				<div class="container">
					<div class="navbar-header">
						<a href="/" class="navbar-brand"><b>Demo查询</b></a>
						<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse">
							<i class="fa fa-bars"></i>
						</button>
					</div>
				</div>
			</nav>
		</header>

		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper">
			<!-- Main content -->
			<div class="container">
				<section class="content-header">
					<c:if test="${successMsg != null}">
						<div class="alert alert-success">
							<button type="button" class="close" data-dismiss="alert">×</button>
								${successMsg}
						</div>
					</c:if>
					<c:if test="${errorMsg != null}">
						<div class="alert alert-error">
							<button type="button" class="close" data-dismiss="alert">×</button>
								${errorMsg}
						</div>
					</c:if>
				</section>
				<section class="content">
					<!-- content -->
					<div class="row">
						<div class="col-md-12">
							<div class="box box-info">
								<div class="box-header with-border">
									<h3 class="box-title">查询</h3>
								</div>
								<form:form action="/" method="POST" modelAttribute="qf">
								<div class="box-body">
									<!-- /input-group -->
									<div class="row">
										<div class="col-xs-8">
											<input name="yun_dan_hao" value="${yun_dan_hao}" type="text" class="form-control" placeholder="请准确输入一条订单号来查询">
										</div>
										<div class="col-xs-2">
											<input name="code" type="text" value="${code}" class="form-control" placeholder="验证码" />
										</div>
										<div class="col-lg-1">
											<img id="validationCodeImg" src="/common/codeimg" alt="验证码" onclick="refreshCode(this);"/>
										</div>
										<div class="col-xs-1">
											<button type="submit" class="btn btn-info btn-flat">查询</button>
										</div>
									</div>
									<!-- /input-group -->
								</div>
								<!-- /.box-body -->
								</form:form>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="box box-warning">
								<div class="box-header with-border">
									<h3 class="box-title">查询结果</h3>
								</div>
								<div class="box-body">
									<!-- 具体的查询结果 -->
										<table class="table table-striped">
											<tbody>
											<tr>
												<th>订单号</th>
												<th>日期</th>
												<th>内容</th>
											</tr>
											<c:forEach items="${yunDans}" var="yunDan">
												<tr>
													<td>${yunDan.yunDanHao}</td>
													<td>${yunDan.shouHuoDate}</td>
													<td>${yunDan.muDiZan}</td>
												</tr>
											</c:forEach>
											</tbody>
										</table>
								</div>
								<!-- /.box-body -->
							</div>
						</div>
					</div>
					<!-- /. content -->
				</section>
			</div>
			<!-- /. Main content -->
		</div>
		<!-- /.content-wrapper -->
		<!-- Main Footer -->
		<footer class="main-footer">
			<div class="container">
				<div class="pull-right hidden-xs">
					<b>Version</b> 1.0.0
					<a href="/admin">进入后台</a>
				</div>
				<strong>Copyright © 2015-2016 Demo系统后台 .</strong>
				<%= DateTime.now().getYear()%>.
			</div>
		</footer>
		<script src="/assets/themes/adminLTE/bootstrap/js/bootstrap.min.js"></script>
		<script src="/assets/themes/adminLTE/adminLTE/js/app.min.js"></script>
		<script src="/assets/common.js"></script>
	</div><!-- ./wrapper -->
</body>
</html>