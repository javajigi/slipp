<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>로그인 :: SLiPP</title>
</head>

<section class="login-content">
	<div class="content-main">
		<h1 class="login-content-title">Sustainable Life, Programming, Programmer</h1>
		<p class="login-content-welcome">삶과 일의 균형을 맞추면서 행복하게 살 수 있는 세상을 꿈꿉니다.</p>
			<form:form action="" method="post" cssClass="signin-with-sns" modelAttribute="signUpForm">
				<p>SLiPP에서 사용할 닉네임을 입력해 주세요.</p>
				<form:input path="userId" cssClass="inp_nickname focused"/><br/>
				<form:errors path="*" />
				<button class="signin-with-sns-submit-btn" type="submit"><i class="icon-signin"></i> 회원가입</button>
			</form:form>
	</div>
	<div class="content-sub">
	</div>
</sction>

<script src="${url:resource('/javascripts/jquery.validate.min.js')}"></script>
<script src="${url:resource('/javascripts/user/form.js')}"></script>
