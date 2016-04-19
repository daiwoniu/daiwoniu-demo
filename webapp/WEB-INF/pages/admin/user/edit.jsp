<jsp:include page="/WEB-INF/pages/themes/adminLTE/header.jsp"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="z" uri="http://taglib.woniu.com"%>
<z:breadcrumb breadcrumb="${breadcrumb}"/>
<h3>编辑${table.Label}</h3>
<form:form action="/admin/user/${userModel.id}/edit" method="POST" validate="true"
	modelAttribute="user" cssClass="basic-form form-horizontal">
	<input type="hidden" name="_method" value="POST" />
	<form:hidden path="id"/>
	<div class="control-group">
		<label class="control-label" for="loginName">登录名</label>
		<div class="controls">
			<form:input path="loginName" class="required" placeholder="登录名" />
			<form:errors path="loginName" cssClass="validation-error" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="name">姓名</label>
		<div class="controls">
			<form:input path="name" class="required" placeholder="姓名" />
			<form:errors path="name" cssClass="validation-error" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="sex">性别</label>
		<div class="controls">
			<%--<form:select path="sex">--%>
				<%--<form:options items="${sexs}" />--%>
			<%--</form:select>--%>
			<form:errors path="sex" cssClass="validation-error" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="mobile">手机</label>
		<div class="controls">
			<form:input path="mobile" class="" placeholder="手机" />
			<form:errors path="mobile" cssClass="validation-error" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="email">邮箱</label>
		<div class="controls">
			<form:input path="email" class="" placeholder="邮箱" />
			<form:errors path="email" cssClass="validation-error" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="addressInfo">地址</label>
		<div class="controls">
			<form:input path="addressInfo" class="" placeholder="地址" />
			<form:errors path="addressInfo" cssClass="validation-error" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="status">用户状态</label>
		<div class="controls">
			<%--<form:select path="status">--%>
				<%--<form:options items="${statuss}" />--%>
			<%--</form:select>--%>
			<form:errors path="status" cssClass="validation-error" />
		</div>
	</div>
	<div class="control-group">
		<div class="controls">
			<button type="submit" class="btn btn-primary">提交</button>
		</div>
	</div>
</form:form>

<jsp:include page="/WEB-INF/pages/themes/adminLTE/footer.jsp"/>
