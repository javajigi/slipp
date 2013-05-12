<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>${socialUser.displayName}의 개인공간 :: SLiPP</title>
</head>

<section class="person-content">
	<div class="person-info">
		<img class="person-info-thumb" src="${sf:stripHttp(socialUser.imageUrl)}" width="80" height="80" alt="" />
		<div class="person-info-text">
			<h1 class="person-info-name">${socialUser.displayName}</h1>
			<a href="/users/changepassword/${socialUser.id}" class="person-info-link-to-change-pw">비밀번호 변경하기</a> <strong>가입일:</strong> yyyy/mm/dd
		</div>
	</div>
	<section class="person-change-pw">
		<form:form modelAttribute="password" cssClass="person-change-pw-form" action="/users/changepassword/${socialUser.id}" method="post">
			<h1>비밀번호 변경</h1>
			<form:hidden path="id"/>
				<div class="box-input-line">
					<p class="person-change-pw-label">현재 비밀번호</p>
					<form:password path="oldPassword" cssClass="inp-title" />
				</div>
				<div class="box-input-line">
					<p class="person-change-pw-label">신규 비밀번호</p>
					<form:password path="newPassword" cssClass="inp-title" />
				</div>
				<div class="box-input-line">
					<p class="person-change-pw-label">신규 비밀번호 확인</p>
					<form:password path="newPasswordConfirm" cssClass="inp-title" />
				</div>
				<c:if test="${not empty errorMessage}">
				<label class="error" style="">${errorMessage}</label>
				</c:if>
				<button type="submit" class="person-change-pw-submit"><i class="icon-key"></i> 비밀번호 변경</button>
			</fieldset>
		</form:form>
	</section>
</section>

<script src="${url:resource('/javascripts/jquery.validate.min.js')}"></script>
<script src="${url:resource('/javascripts/user/changepassword.js')}"></script>
