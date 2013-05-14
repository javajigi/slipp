<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>${socialUser.displayName}의 개인공간 :: SLiPP</title>
</head>

<section class="person-content">
	<slipp:profile-header socialUser="${socialUser}"/>
	<section class="sign-in-to-slipp">
		<form:form modelAttribute="user" cssClass="form-write" action="/users/${socialUser.id}" method="put">
				<form:input path="userId" class="inp_id" placeholder="아이디" />
				<form:input path="nickName" class="inp_pw" placeholder="닉네임" />
				<form:input path="email" class="inp_email" placeholder="이메일" />
				<button type="submit" class="sign-in-to-slipp-btn"><i class="icon-signin"></i>개인정보수정</button>
		</form:form>
	</section>
</section>

<script src="${url:resource('/javascripts/jquery.validate.min.js')}"></script>
<script src="${url:resource('/javascripts/user/login.js')}"></script>