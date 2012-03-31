<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
<link href="${url:resource('/stylesheets/boards.css')}" rel="stylesheet">
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="span4 offset4">
				<h2>SLiPP에서 사용할 계정</h2>
				<form:form action="" method="post" modelAttribute="signUpForm" cssClass="well form-search">
					<form:input path="userName" cssClass="input-medium "/>
					<input class="btn btn-success" type="submit" value="로그인" />
					<form:errors path="*" />
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>