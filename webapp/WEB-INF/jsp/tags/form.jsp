<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>

<head>
<link rel="stylesheet" href="${url:resource('/stylesheets/wiki-style.css')}">
<link rel="stylesheet" href="${url:resource('/stylesheets/wiki-textile-style.css')}">
</head>

<section class="write-content">
	<div class="content-main">
		<h1 class="write-title">태그 추가 요청</h1>
		<form:form modelAttribute="tag" cssClass="form-write" action="/tags" method="POST">
			<fieldset>
				<div class="box-input-line">
					<form:input path="email" cssClass="inp-title" placeholder="태그 관리자 이메일" />
				</div>
				<div class="box-input-line">
					<form:input path="name" cssClass="inp-title" placeholder="태그명" />
				</div>
				<div class="box-write">
					<form:textarea path="description"  cols="80" rows="15"/>
				</div>
				<c:if test="${loginUser.facebookUser}">
				<div class="box-input-line">
					태그와 연결할 페이스북 그룹<br/>
					<form:radiobuttons path="groupId" items="${fbGroups}" itemLabel="name" itemValue="groupId" />
				</div>
				</c:if>
				<div class="submit-write">
					<button type="submit" class="btn-submit"><i class="icon-submit"></i> 추가 요청</button>
				</div>
			</fieldset>
		</form:form>
	</div>
	<div class="content-sub">
	</div>
</div>

<script src="${url:resource('/javascripts/jquery.validate.min.js')}"></script>
<script src="${url:resource('/javascripts/support/slipp.validate.js')}"></script>
<script src="${url:resource('/javascripts/tag/form.js')}"></script>
<script src="${url:resource('/javascripts/highlight.pack.js')}"></script>
<script src="${url:resource('/javascripts/qna/qna-set.js')}"></script>
<script src="${url:resource('/javascripts/qna/image.upload.js')}"></script>
<script src="${url:resource('/javascripts/jquery.markitup.js')}"></script>
<script>
$('#description').markItUp(mySettings);
</script>
