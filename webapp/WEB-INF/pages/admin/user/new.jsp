<jsp:include page="/WEB-INF/pages/themes/adminLTE/header.jsp"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="z" uri="http://taglib.woniu.com"%>
<z:breadcrumb breadcrumb="${breadcrumb}"/>
<h3>添加用户</h3>
<form:form action="/admin/user/new" method="POST" validate="true"
	modelAttribute="user" cssClass="basic-form form-horizontal">
	<div class="row">
		<div class="control-group col-md-6">
			<label class="control-label " for="loginName">登录名</label>
			<div class="controls">
				<form:input path="loginName" class="required" placeholder="登录名" />&nbsp;<span style="color:#FF0000">*</span>
				<form:errors path="loginName" cssClass="validation-error" />
			</div>
		</div>
		<div class="control-group col-md-6">
			<label class="control-label" for="name">姓名</label>
			<div class="controls">
				<form:input path="name" class="required" placeholder="姓名" />&nbsp;<span style="color:#FF0000">*</span>
				<form:errors path="name" cssClass="validation-error" />
			</div>
		</div>
	</div>
	<div class="row">
		<div class="control-group col-md-6">
			<label class="control-label" for="loginPassword">密码</label>
			<div class="controls">
				<form:password path="loginPassword" class="required" placeholder="密码" />&nbsp;<span style="color:#FF0000">*</span>
				<form:errors path="loginPassword" cssClass="validation-error" />
			</div>
		</div>
		<div class="control-group col-md-6">
			<label class="control-label" for="confirmPassword">确认密码</label>
			<div class="controls">
				<form:password path="confirmPassword" class="required" placeholder="确认密码" />&nbsp;<span style="color:#FF0000">*</span>
				<form:errors path="confirmPassword" cssClass="validation-error" />
			</div>
		</div>
    </div>
	<%--<div class="row">--%>
		<%--<div class="control-group col-md-6">--%>
			<%--<label class="control-label" for="sex">性别</label>--%>
			<%--<div class="controls">--%>
				<%--&lt;%&ndash;<form:select path="sex">&ndash;%&gt;--%>
					<%--&lt;%&ndash;<form:options items="${sexs}" />&ndash;%&gt;--%>
				<%--&lt;%&ndash;</form:select>&ndash;%&gt;--%>
				<%--<form:errors path="sex" cssClass="validation-error" />--%>
			<%--</div>--%>
		<%--</div>--%>
	<%--</div>--%>
	<div class="row">
		<div class="control-group col-md-6">
			<label class="control-label" for="mobile">手机</label>
			<div class="controls">
				<form:input path="mobile" class="" placeholder="手机" />
				<form:errors path="mobile" cssClass="validation-error" />
			</div>
		</div>
		<div class="control-group col-md-6">
			<label class="control-label" for="email">邮箱</label>
			<div class="controls">
				<form:input path="email" class="" placeholder="邮箱" />
				<form:errors path="email" cssClass="validation-error" />
			</div>
		</div>
	</div>
	<div class="row">
		<div class="control-group col-md-6">
			<label class="control-label" for="addressInfo">地址</label>
			<div class="controls">
				<form:input path="addressInfo" class="" placeholder="地址" />
				<form:errors path="addressInfo" cssClass="validation-error" />
			</div>
		</div>
	</div>
	<div class="control-group">
		<div class="controls">
			<button type="submit" class="btn btn-primary">提交</button>
			<button type="reset" class="btn">重置</button>
		</div>
	</div>
</form:form>
<jsp:include page="/WEB-INF/pages/themes/adminLTE/footer.jsp"/>
