<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>로그인 :: SLiPP</title>
</head>

<section class="login-content">
	<div class="content-main">
		<h1>Sustainable Life, Programming, Programmer</h1>
		<p>삶과 일의 균형을 맞추면서 행복하게 살 수 있는 세상을 꿈꿉니다.</p>
		<p class="text-choose-login-path">로그인 방식을 선택해주세요.</p>
		<form action="/signin/facebook" method="POST">
			<input type="hidden" name="scope" value="publish_stream" />
			<button type="submit" class="btn-login-facebook"><i class="foundicon-facebook"></i> 페이스북</button>
		</form>
		<form action="/signin/twitter" method="POST">
			<button type="submit" class="btn-login-twitter"><i class="foundicon-twitter"></i> 트위터</button>
		</form>
		<form action="/signin/google?scope=https://www.googleapis.com/auth/userinfo.profile" method="POST">
			<button type="submit" class="btn-login-google"><i class="foundicon-google-plus"></i> 구글</button>
		</form>
	</div>
	<div class="content-sub">
	</div>
</sction>
