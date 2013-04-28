<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>

<head>
</head>

<section class="write-content">
	<div class="content-main">
		<form:form modelAttribute="user" cssClass="form-write" action="/users" method="post">
			<fieldset>
				<div class="box-input-line">
					이메일 주소 : <form:input path="email" cssClass="inp-title" placeholder="이메일 주소" />
				</div>
				<div class="box-input-line">
					SLiPP에서 활동할 계정 : <form:input path="username" cssClass="inp-title" placeholder="" />
				</div>
				<div class="submit-write">
					<button type="submit" class="btn-submit"><i class="icon-submit"></i> 회원가입</button>
				</div>
			</fieldset>
		</form:form>
	</div>
	<div class="content-sub">
	</div>
</div>
