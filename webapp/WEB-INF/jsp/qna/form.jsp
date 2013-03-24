<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>

<head>
<link href="${url:resource('/stylesheets/wiki-style.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-textile-style.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-imageupload-plugins.css')}" rel="stylesheet">
</head>

<section class="write-content">
	<div class="content-main">
		<h1 class="write-title">새 글 작성</h1>
		<c:choose>
			<c:when test="${not empty question.questionId}">
				<c:set var="method" value="PUT" />
			</c:when>
			<c:otherwise>
				<c:set var="method" value="POST" />
			</c:otherwise>
		</c:choose>
		<form:form modelAttribute="question" cssClass="form-write" action="/questions" method="${method}">
			<form:hidden path="questionId"/>
			<fieldset>
				<div class="box-input-line">
					<form:input path="title" cssClass="inp-title" placeholder="제목" />
				</div>
				<div class="box-write">
					<form:textarea path="contents"  cols="80" rows="5"/>
				</div>
				<div class="box-input-line">
					<form:input path="plainTags" cssClass="inp-tags" placeholder="태그 - 공백 또는 쉼표로 구분 ex) javajigi, slipp" />
				</div>
				<div class="submit-write">
					<c:if test="${loginUser.facebookUser and empty question.questionId}">
					<label class="msg-send-to-facebook">
						<form:checkbox path="connected" /> 페이스북으로 전송
					</label>
					</c:if>
					<button type="submit" class="btn-submit"><i class="icon-submit"></i> 작성완료</button>
				</div>
			</fieldset>
		</form:form>
	</div>
	<div class="content-sub">
	</div>
</div>

<script src="${url:resource('/javascripts/jquery.validate.min.js')}"></script>
<script src="${url:resource('/javascripts/highlight.pack.js')}"></script>
<script src="${url:resource('/javascripts/jquery.markitup.js')}"></script>
<script src="${url:resource('/javascripts/qna/image.upload.js')}"></script>
<script src="${url:resource('/javascripts/qna/qna-set.js')}"></script>
<script src="${url:resource('/javascripts/qna/tagparser.js')}"></script>
<script src="${url:resource('/javascripts/jquery.autocomplete.min.js')}"></script>
<script src="${url:resource('/javascripts/qna/form.js')}"></script>
<script src="${url:resource('/javascripts/qna/write.js')}"></script>
<script>
$('#contents').markItUp(mySettings);
</script>
