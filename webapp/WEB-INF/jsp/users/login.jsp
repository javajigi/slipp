<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>로그인 :: SLiPP</title>
</head>

<section class="login-content">
	<div class="content-main">
		<h1 class="login-content-title">Sustainable Life, Programming, Programmer</h1>
		<p class="login-content-welcome">삶과 일의 균형을 맞추면서 행복하게 살 수 있는 세상을 꿈꿉니다.</p>
		<div class="choose-login-type">
			<div class="login-with-slipp">
				<p class="login-with-slipp-text">SLiPP 계정으로 로그인하세요.</p>
				<form id="authentication" action="/users/authenticate" method="POST">
					<input id="authenticationId" name="authenticationId" class="inp-id" placeholder="아이디" type="text" value=""/>
					<input id="authenticationPassword"  name="authenticationPassword" class="inp-title" placeholder="비밀번호" type="password" value=""/>
					<c:if test="${not empty param.login_error}">
					<label class="error" style="">아이디, 비밀번호가 정확하지 않습니다.</label>
					</c:if>
					<p class="login-with-slipp-submit">
						<label><input name="_spring_security_remember_me" class="inp-pw" type="checkbox" value="true"/> 자동로그인</label>
						<button id="loginSubmitBtn" type="submit" class="login-with-slipp-submit-btn"><i class="icon-login"></i> 로그인</button>
					</p>
				</form>
			</div>
			<div class="divide-bar left"></div>
			<div class="login-with-sns">
				<p class="login-with-sns-text">또는, SNS 계정으로 로그인하세요.</p>
				<form action="/signin/facebook">
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
			<div class="divide-bar right"></div>
			<div class="sign-in-to-slipp">
				<p class="sign-in-to-slipp-text">계정이 없다면 간단히 만들어보세요.</p>
				<form:form modelAttribute="user" cssClass="form-write" action="/users" method="post">
						<form:input path="userId" class="inp_id" placeholder="아이디" />
						<form:input path="nickName" class="inp_pw" placeholder="닉네임" />
						<form:input path="email" class="inp_email" placeholder="이메일" />
						<p class="sign-in-to-slipp-notice">패스워드는 등록한 메일주소로 발송되며, 로그인 후에 개인페이지에서 수정 가능합니다.</p>
						<button type="submit" class="sign-in-to-slipp-btn"><i class="icon-signin"></i> 회원가입</button>
				</form:form>
			</div>
		</div>
	</div>
	<div class="content-sub">
	</div>
</sction>

<script src="${url:resource('/javascripts/jquery.validate.min.js')}"></script>
<script src="${url:resource('/javascripts/user/login.js')}"></script>
