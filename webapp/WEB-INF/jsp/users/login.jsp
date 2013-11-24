<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>로그인 :: SLiPP</title>
</head>

<section class="login-content">
	<div class="content-main">
		<h1 class="login-content-title">Sustainable Life, Programming, Programmer</h1>
		<p class="login-content-welcome">삶과 일의 균형을 맞추면서 행복하게 살 수 있는 세상을 꿈꿉니다.</p>
		<slipp:loginform redirectUrl="/"/>
	</div>
	<div class="content-sub">
	</div>
</sction>

<script src="${url:resource('/javascripts/jquery.validate.min.js')}"></script>
<script src="${url:resource('/javascripts/support/slipp.validate.js')}"></script>
<script src="${url:resource('/javascripts/user/login.js')}"></script>
