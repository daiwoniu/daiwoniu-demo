<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="org.joda.time.DateTime" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<title>浪淘沙</title>
	<link href="/assets/themes/adminLTE/adminLTE/css/AdminLTE.min.css" rel="stylesheet">
	<link href="/assets/themes/adminLTE/adminLTE/css/skins/_all-skins.min.css" rel="stylesheet">
	<link href="/assets/themes/adminLTE/bootstrap/css/bootstrap.min.css" rel="stylesheet">

	<link href="/assets/common.css" rel="stylesheet">

	<!--[if lt IE 9]><!--<script src="/assets/html5shiv/dist/html5shiv.js"></script>--><![endif]-->

	<script src="/assets/themes/adminLTE/jquery/jquery.min.js"></script>
</head>
<body class="skin-purple layout-top-nav">
	<div class="wrapper">
		<!-- Main Header -->
		<header class="main-header">
			<!-- Header Navbar -->
			<nav class="navbar navbar-static-top">
				<div class="container">
					<div class="navbar-header">
						<a href="/" class="navbar-brand"><b>渠道查询</b></a>
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
							<div class="nav-tabs-custom">
								<ul class="nav nav-tabs">
									<li class="header"><i class="fa fa-th"></i><b>渠道查询</b>&nbsp;&nbsp;&nbsp; </li>
									<li class="active"><a href="#tab_sina_weiboes" data-toggle="tab">新浪微博</a></li>
									<li class=""><a href="#tab_wechat_publics" data-toggle="tab">微信公众号</a></li>
									<li class=""><a href="#tab_miao_pais" data-toggle="tab">秒拍</a></li>
									<li class=""><a href="#tab_mei_pais" data-toggle="tab">美拍</a></li>
									<%--<li class="pull-right"><a href="#" class="text-muted"><i class="fa fa-gear"></i></a></li>--%>
								</ul>
								<div class="tab-content">
									<div class="tab-pane active" id="tab_1">
										<form class="form-horizontal">
											<input type="hidden" id="qdType" value="sina_weiboes">
											<div class="box-body">
												<div class="form-group">
													<label class="col-sm-2 control-label">常见分类：</label>
													<div class="col-sm-10">
														<div class="btn-group">
															<button name="selectCommonClasses" type="button" class="btn btn-default btn-query-group all active" />不限</button>
															<button name="selectCommonClasses" type="button" class="btn btn-default btn-query-group" value="分类1" />分类1</button>
															<button name="selectCommonClasses" type="button" class="btn btn-default btn-query-group" value="分类1" />分类1</button>
															<button name="selectCommonClasses" type="button" class="btn btn-default btn-query-group" value="分类1" />分类1</button>
															<button name="selectCommonClasses" type="button" class="btn btn-default btn-query-group" value="分类1" />分类1</button>
															<button name="selectCommonClasses" type="button" class="btn btn-default btn-query-group" value="分类1" />分类1</button>
															<button name="selectCommonClasses" type="button" class="btn btn-default btn-query-group" value="分类1" />分类1</button>
															<button name="selectCommonClasses" type="button" class="btn btn-default btn-query-group" value="分类1" />分类1</button>
															<button name="selectCommonClasses" type="button" class="btn btn-default btn-query-group" value="分类1" />分类1</button>
															<button name="selectCommonClasses" type="button" class="btn btn-default btn-query-group" value="分类1" />分类1</button>
															<button name="selectCommonClasses" type="button" class="btn btn-default btn-query-group" value="分类1" />分类1</button>
															<button name="selectCommonClasses" type="button" class="btn btn-default btn-query-group" value="分类1" />分类1</button>
															<button name="selectCommonClasses" type="button" class="btn btn-default btn-query-group" value="分类1" />分类1</button>
															<button name="selectCommonClasses" type="button" class="btn btn-default btn-query-group" value="分类1" />分类1</button>
															<button name="selectCommonClasses" type="button" class="btn btn-default btn-query-group" value="分类1" />分类1</button>
														</div>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-2 control-label">粉丝数：</label>
													<div class="col-sm-10">
														<div class="btn-group">
															<button name="selectFansCount" type="button" class="btn btn-default btn-query-group all active" />不限</button>
															<button name="selectFansCount" type="button" class="btn btn-default btn-query-group" value="-10000" />一万以下</button>
															<button name="selectFansCount" type="button" class="btn btn-default btn-query-group" value="10000-50000" />1-5万</button>
															<button name="selectFansCount" type="button" class="btn btn-default btn-query-group" value="50000-100000" />5-10万</button>
															<button name="selectFansCount" type="button" class="btn btn-default btn-query-group" value="100000-200000" />10-20万</button>
															<button name="selectFansCount" type="button" class="btn btn-default btn-query-group" value="200000-400000" />20-40万</button>
															<button name="selectFansCount" type="button" class="btn btn-default btn-query-group" value="400000-800000" />40-80万</button>
															<button name="selectFansCount" type="button" class="btn btn-default btn-query-group" value="800000-" />80万以上</button>
														</div>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-2 control-label">价格：</label>
													<div class="col-sm-10">
														<div class="btn-group">
															<button name="selectQuotation" type="button" class="btn btn-default btn-query-group all active" />不限</button>
															<button name="selectQuotation" type="button" class="btn btn-default btn-query-group" value="-100" />100以下</button>
															<button name="selectQuotation" type="button" class="btn btn-default btn-query-group" value="100-200" />100-200</button>
															<button name="selectQuotation" type="button" class="btn btn-default btn-query-group" value="200-500" />200-500</button>
															<button name="selectQuotation" type="button" class="btn btn-default btn-query-group" value="500-1000" />500-1000</button>
															<button name="selectQuotation" type="button" class="btn btn-default btn-query-group" value="1000-5000" />1000-5000</button>
															<button name="selectQuotation" type="button" class="btn btn-default btn-query-group" value="5000-" />5000以上</button>
														</div>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-2 control-label">其他筛选：</label>
													<div class="col-sm-10">
														<div class="checkbox">
															<label>
																<input type="checkbox" id="is_planning">
																有没有定制策划服务
															</label>
														</div>
													</div>
												</div>
											</div>
										</form>
										<div class="box box-info" style="width: 90%; margin-left: auto; margin-right: auto;">
											<div class="box-header">
												<b>筛选出来的渠道：</b>
											</div>
											<div class="box-body" style="padding-left: 80px;">
												<div class="row">
													<div class="col-md-2">
														<img src="ddfsd" />
													</div>
													<div class="col-md-2">
														<dl>
															<dt>小李子</dt>
															<dd>新浪:<a href="#">lizi</a></dd>
														</dl>
													</div>
													<div class="col-md-2">
														<dl>
															<dt>粉丝数</dt>
															<dd>10</dd>
														</dl>
													</div>
													<div class="col-md-2">
														<dl>
															<dt>位置</dt>
															<dd>首页第一位</dd>
														</dl>
													</div>
													<div class="col-md-2">
														<dl>
															<dt>参考报价</dt>
															<dd>10</dd>
														</dl>
													</div>
													<div class="col-md-2">
														<dl>
															<dt>阅读量</dt>
															<dd>10</dd>
														</dl>
													</div>
												</div>
												<div class="row">
													<div class="col-md-12">
														成功案例： 韩束补水
													</div>
												</div>
												<hr style="border-color: #1087dd;">
												<div class="row">
													<div class="col-md-2">
														<img src="ddfsd" />
													</div>
													<div class="col-md-2">
														<dl>
															<dt>小凳子</dt>
															<dd>新浪:<a href="#">dengzi</a></dd>
														</dl>
													</div>
													<div class="col-md-2">
														<dl>
															<dt>粉丝数</dt>
															<dd>10</dd>
														</dl>
													</div>
													<div class="col-md-2">
														<dl>
															<dt>位置</dt>
															<dd>首页第一位</dd>
														</dl>
													</div>
													<div class="col-md-2">
														<dl>
															<dt>参考报价</dt>
															<dd>10</dd>
														</dl>
													</div>
													<div class="col-md-2">
														<dl>
															<dt>阅读量</dt>
															<dd>10</dd>
														</dl>
													</div>
												</div>
												<div class="row">
													<div class="col-md-12">
														成功案例： 韩束补水
													</div>
												</div>
												<hr style="border-color: #1087dd;">
											</div>
										</div>
									</div>
									<!-- /.tab-pane -->
								</div>
								<!-- /.tab-content -->
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
				<strong>Copyright © 2015-2016 浪淘沙系统 .</strong>
				<%= DateTime.now().getYear()%>.
			</div>
		</footer>
		<script src="/assets/themes/adminLTE/bootstrap/js/bootstrap.min.js"></script>
		<script src="/assets/themes/adminLTE/adminLTE/js/app.min.js"></script>
		<script src="/assets/common.js"></script>
		<script src="/assets/front.js"></script>
	</div><!-- ./wrapper -->
</body>
</html>