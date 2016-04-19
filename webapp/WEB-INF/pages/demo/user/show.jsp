<jsp:include page="/WEB-INF/pages/themes/adminLTE/header.jsp"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="z" uri="http://taglib.woniu.com"%>
<z:breadcrumb breadcrumb="${breadcrumb}"/>
<table class="table table-striped table-bordered">
	<tr><td>登录名</td><td>${user.loginName}</td></tr>
	<tr><td>token</td><td>${user.token}</td></tr>
	<tr><td>姓名</td><td>${user.name}</td></tr>
	<tr><td>性别</td><td>${user.sex}</td></tr>
	<tr><td>手机</td><td>${user.mobile}</td></tr>
	<tr><td>邮箱</td><td>${user.email}</td></tr>
	<tr><td>地址</td><td>${user.addressInfo}</td></tr>
	<tr><td>用户状态</td><td>${user.status}</td></tr>
</table>
<a href="<c:url value="/admin/user/${user.id}/edit"/>" class="btn btn-primary">编辑</a>
<a href="<c:url value="/admin/user/${user.id}/reset_password"/>" class="btn btn-warning">重置密码</a>
<%--<a href="<c:url value="/admin/user/${user.id}"/>" class="btn btn-danger" data-method="DELETE" data-remote="true" data-redirect="<c:url value="/admin/user"/>">删除</a>--%>
<jsp:include page="/WEB-INF/pages/themes/adminLTE/footer.jsp"/>
