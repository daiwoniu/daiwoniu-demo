<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<title>Demo系统</title>
	<link href="/assets/themes/adminLTE/adminLTE/css/AdminLTE.min.css" rel="stylesheet">
	<link href="/assets/themes/adminLTE/adminLTE/css/skins/_all-skins.min.css" rel="stylesheet">
	<link href="/assets/themes/adminLTE/bootstrap/css/bootstrap.css" rel="stylesheet">

	<link href="/assets/common.css" rel="stylesheet">

	<!--[if lt IE 9]><!--<script src="/assets/html5shiv/dist/html5shiv.js"></script>--><![endif]-->

	<script src="/assets/themes/adminLTE/jquery/jquery.min.js"></script>
</head>
<body class="skin-blue">
<div class="wrapper">

	<!-- Main Header -->
	<header class="main-header">
		<!-- Logo -->
		<a href="/admin" class="logo"><b>Demo后台</b></a>
		<!-- Header Navbar -->
		<nav class="navbar navbar-static-top">
			<!-- Sidebar toggle button-->
			<!-- 显示隐藏左侧菜单-->
			<a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
				<span class="sr-only">Toggle navigation</span>
			</a>

			<div class="navbar-custom-menu">
				<ul class="nav navbar-nav">
					<!-- User Account Menu -->
					<!-- 头部用户信息显示 -->
					<li class="dropdown user user-menu">
						<!-- Menu Toggle Button -->
						<a href="#" class="dropdown-toggle" data-toggle="dropdown">
							<!-- The user image in the navbar-->
							<!-- 用户头像 -->
							<%--<img src="" class="user-image" alt="User Image"/>--%>
							<!-- hidden-xs hides the username on small devices so only the image appears. -->
							<i class="glyphicon glyphicon-user"></i>
							<!-- 用户名称 -->
							<span class="hidden-xs">${userContext.user.name}</span>
						</a>
						<ul class="dropdown-menu">
							<!-- The user image in the menu -->
							<li class="user-header" style="height: auto;">
								<!-- 用户头像 -->
								<%--<img src="" class="img-circle" alt="User Image" />--%>
								<a href="/admin/user/${userContext.user.id}"><i class="glyphicon glyphicon-user"></i></a>
								<p>
									<!-- 用户角色信息 -->
									${userContext.user.name}
									<small>${userContext.user.createDate} &nbsp;注册</small>
								</p>
							</li>
							<li class="user-footer">
								<div class="pull-left">
									<a href="/admin/user/change_password" class="btn btn-default btn-flat">修改密码</a>
								</div>
								<div class="pull-right">
									<a href="/admin/logout" class="btn btn-default btn-flat">退出</a>
								</div>
							</li>
						</ul>
					</li>
				</ul>
			</div>
		</nav>
	</header>

	<jsp:include page="/WEB-INF/pages/themes/adminLTE/sidebar.jsp"/>

	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Main content -->
		<div class="content-header">
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
		</div>
		<div class="content">
			<!-- content -->
