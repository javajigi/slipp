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
		<br/><br/>		
		<form:form modelAttribute="user" cssClass="form-write" action="/users" method="post">
			<fieldset>
				<div class="box-input-line">
					아이디 : <form:input path="userId" cssClass="inp-title" placeholder="아이디" />
				</div>			
				<div class="box-input-line">
					닉네임 : <form:input path="nickName" cssClass="inp-title" placeholder="닉네임" />
				</div>
				<div class="box-input-line">
					이메일 주소 : <form:input path="email" cssClass="inp-title" placeholder="이메일 주소" />
				</div>				
				<div class="submit-write">
					<button type="submit" class="btn-submit"><i class="icon-submit"></i> 회원가입</button>
				</div>
			</fieldset>
		</form:form>
		<form action="/users/authenticate" class="form-write" method="POST">
			<fieldset>
				<div class="box-input-line">
					아이디 : <input id="login-userId" name="userId" class="inp-title" placeholder="아이디" type="text" value=""/>
				</div>
				<div class="box-input-line">
					비밀번호 : <input id="login-password"  name="password" class="inp-title" placeholder="비밀번호" type="text" value=""/>
				</div>
				<div class="box-input-line">
					자동 로그인 : <input name="_spring_security_remember_me" class="inp-title" type="checkbox" value="true"/>
				</div>				
				<div class="submit-write">
					<button id="loginSubmitBtn" type="submit" class="btn-submit"><i class="icon-submit"></i> 로그인</button>
				</div>
			</fieldset>		
		</form>	
	</div>
	<div class="content-sub">
	</div>
</sction>

<script src="${url:resource('/javascripts/jquery.validate.min.js')}"></script>
<script src="${url:resource('/javascripts/user/login.js')}"></script>
