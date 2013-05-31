<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>${socialUser.userId}의 개인공간 :: SLiPP</title>
</head>

<section class="person-content">
	<slipp:profile-header socialUser="${socialUser}"/>
	<section class="person-change-account-setting">
		<form:form id="modifyUser" modelAttribute="user" cssClass="person-change-account-setting-form" action="/users/${socialUser.id}" method="put">
			<h1>개인정보 변경</h1>
			<div class="box-input-line">
				<p class="person-change-account-setting-label">이메일</p>
				<form:input path="email" class="inp_email" />
			</div>
			<div class="box-input-line">
				<p class="person-change-account-setting-label">닉네임</p>
				<form:input path="userId" class="inp_id" placeholder="아이디" />
			</div>
			<c:if test="${socialUser.SLiPPUser}">
			<div class="box-input-line">
				<p class="person-change-account-setting-label">프로필이미지는
				<a href="http://gravatar.com" target="_blank">Gravatar</a>를 사용합니다.</p>
			</div>
			</c:if>
			<button type="submit" class="person-change-account-setting-submit"><i class="icon-login"></i> 개인정보수정</button>
		</form:form>
	</section>
</section>

<script src="${url:resource('/javascripts/jquery.validate.min.js')}"></script>
<script src="${url:resource('/javascripts/support/slipp.validate.js')}"></script>
<script src="${url:resource('/javascripts/user/login.js')}"></script>
