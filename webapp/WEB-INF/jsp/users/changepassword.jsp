<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>${socialUser.userId}의 개인공간 :: SLiPP</title>
</head>

<section class="person-content">
	<slipp:profile-header socialUser="${socialUser}"/>
	<section class="person-change-pw">
		<form:form modelAttribute="password" cssClass="person-change-pw-form" action="/users/${socialUser.id}/changepassword" method="post">
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
