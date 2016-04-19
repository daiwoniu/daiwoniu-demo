<jsp:include page="/WEB-INF/pages/themes/adminLTE/header.jsp"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@taglib prefix="z" uri="http://taglib.woniu.com"%>
<z:breadcrumb breadcrumb="${breadcrumb}"/>
<div class="row-fluid clearfix">
	<div class="pull-left">
		<h3>用户查询</h3>
	</div>
	<div class="pull-right">
		<a class="btn btn-primary" href="<c:url value="/admin/user/new"/>">创建用户</a>
	</div>
</div>
<div class="row-fluid">
	<form:form action="/admin/user" method="GET"
			   modelAttribute="qf" cssClass="basic-form form-inline">
		<div class="form-group">
			<label>登录名</label>
			<input type="text" name="login_name" value="${ qf.get("login_name") }" class="" placeholder="登录名" />
		</div>&nbsp;&nbsp;
		<div class="form-group">
			<label>名称</label>
			<input type="text" name="name" value="${ qf.get("name") }" class="" placeholder="名称" />
		</div>&nbsp;&nbsp;
		<div class="form-group">
			<label>邮箱</label>
			<input type="text" name="email" value="${ qf.get("email") }" class="" placeholder="邮箱" />
		</div>&nbsp;&nbsp;
		<div class="form-group">
			<button type="submit" class="btn btn-primary">查询</button>
		</div>
	</form:form>
</div>
<display:table name="users.data" id="user_" class="table table-striped table-bordered">
	<display:column title="登录名">
		<a href="/admin/user/${user_.id}">${user_.loginName}</a>
	</display:column>
	<display:column property="name" title="名称" />
	<%--<display:column title="性别">--%>
		<%--${sexs.get(user_.sex)}--%>
	<%--</display:column>--%>
	<display:column property="mobile" title="手机" />
	<display:column property="email" title="邮箱" />
	<display:column property="addressInfo" title="地址" />
	<%--<display:column title="状态">--%>
		<%--<c:choose>--%>
			<%--<c:when test="${statuss.get(user_.status)=='正常'}">--%>
				<%--<span class="label label-success">${statuss.get(user_.status)}</span>--%>
			<%--</c:when>--%>
			<%--<c:when test="${statuss.get(user_.status)=='禁用或失效'}">--%>
				<%--<span class="label label-warning">${statuss.get(user_.status)}</span>--%>
			<%--</c:when>--%>
			<%--<c:otherwise>--%>
				<%--<span class="label label-danger">${statuss.get(user_.status)}</span>--%>
			<%--</c:otherwise>--%>
		<%--</c:choose>--%>
	<%--</display:column>--%>
</display:table>
<z:pagination name="users" css="demoPagination" />
<jsp:include page="/WEB-INF/pages/themes/adminLTE/footer.jsp"/>
