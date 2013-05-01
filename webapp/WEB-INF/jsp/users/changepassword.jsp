<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>${socialUser.displayName} 공간 :: SLiPP</title>
</head>

<img class="user-thumb" src="${sf:stripHttp(socialUser.imageUrl)}" width="80" height="80" alt="" />

display name : ${socialUser.displayName} 

<section class="login-content">
	<div class="content-main">
		<form action="/users/changepassword/${socialUser.id}" class="form-write" method="POST">
			<fieldset>
				<div class="box-input-line">
					이전 비밀번호 : <input id="oldPassword" name="oldPassword" class="inp-title" placeholder="이전 비밀번호" type="password" value=""/>
				</div>
				<div class="box-input-line">
					신규 비밀번호 : <input id="newPassword" name="newPassword" class="inp-title" placeholder="신규 비밀번호" type="password" value=""/>
				</div>
				<div class="box-input-line">
					신규 비밀번호 확인 : <input id="newPasswordConfirm" name="newPasswordConfirm" class="inp-title" placeholder="신규 비밀번호 확인" type="password" value=""/>
				</div>				
				<div class="submit-write">
					<button type="submit" class="btn-submit"><i class="icon-submit"></i> 비밀번호 변경</button>
				</div>
			</fieldset>		
		</form>	
	</div>
</section>