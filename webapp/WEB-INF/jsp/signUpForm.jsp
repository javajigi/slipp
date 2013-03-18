<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>로그인 :: SLiPP</title>
	<link href="${url:resource('/stylesheets/main.css')}" rel="stylesheet">
</head>

<div class="jumbotron">
	<div class="container">
		<h2>Sustainable Life, Programming, Programmer</h2>
		<p>삶과 일의 균형을 맞추면서 행복하게 살 수 있는 세상을 꿈꿉니다.</p>

		<div class="span6 offset2">
			<form:form action="" method="post" modelAttribute="signUpForm" cssClass="form-horizontal">
				<fieldset>
					<div class="control-group">
						<div class="controls">
							SLiPP에서 활동할 계정 : <form:input path="username" cssClass="input-xlarge focused span2"/>
							<form:errors path="*" />
							<input class="btn btn-success" type="submit" value="로그인" />
						</div>
					</div>
				</fieldset>
			</form:form>
		</div>
	</div>
</div>
<script src="http://ajax.microsoft.com/ajax/jquery.validate/1.7/jquery.validate.min.js"></script>
<script type="text/javascript"	src="${url:resource('/javascripts/user/form.js')}"></script>
