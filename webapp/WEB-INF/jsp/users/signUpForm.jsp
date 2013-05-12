<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>로그인 :: SLiPP</title>
</head>

<section class="login-content">
	<div class="content-main">
		<h1>Sustainable Life, Programming, Programmer</h1>
		<p>삶과 일의 균형을 맞추면서 행복하게 살 수 있는 세상을 꿈꿉니다.</p>
			<form:form action="" method="post" modelAttribute="signUpForm" cssClass="form-horizontal">
				<fieldset>
					<div class="control-group">
						<div class="controls">
							사용자 아이디 : <form:input path="userId" cssClass="focused span2"/><br/>
							닉네임 : <form:input path="nickName" cssClass="focused span2"/><br/>
							<form:errors path="*" />
							<input class="btn btn-success" type="submit" value="로그인" />
						</div>
					</div>
				</fieldset>
			</form:form>
	</div>
	<div class="content-sub">
	</div>
</sction>

<script src="${url:resource('/javascripts/jquery.validate.min.js')}"></script>
<script src="${url:resource('/javascripts/user/form.js')}"></script>
