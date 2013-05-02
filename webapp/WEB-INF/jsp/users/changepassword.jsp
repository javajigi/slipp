<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>${socialUser.displayName} 공간 :: SLiPP</title>
</head>

<img class="user-thumb" src="${sf:stripHttp(socialUser.imageUrl)}" width="80" height="80" alt="" />

display name : ${socialUser.displayName} 

<section class="login-content">
	<div class="content-main">
		<form:form modelAttribute="password" cssClass="form-write" action="/users/changepassword/${socialUser.id}" method="post">
			<form:hidden path="id"/>
			<fieldset>
				<div class="box-input-line">
					이전 비밀번호 : <form:input path="oldPassword" cssClass="inp-title" placeholder="이전 비밀번호" />
				</div>
				<div class="box-input-line">
					신규 비밀번호 : <form:input path="newPassword" cssClass="inp-title" placeholder="신규 비밀번호" />
				</div>
				<div class="box-input-line">
					신규 비밀번호 확인 : <form:input path="newPasswordConfirm" cssClass="inp-title" placeholder="신규 비밀번호 확인" />
				</div>				
				<div class="submit-write">
					<button type="submit" class="btn-submit"><i class="icon-submit"></i> 비밀번호 변경</button>
				</div>
			</fieldset>		
		</form:form>	
	</div>
</section>