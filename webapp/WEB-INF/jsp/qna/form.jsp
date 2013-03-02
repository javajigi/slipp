<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<link href="${url:resource('/stylesheets/wiki-style.css')}" rel="stylesheet">
	<link href="${url:resource('/stylesheets/wiki-textile-style.css')}" rel="stylesheet">
	<link href="${url:resource('/stylesheets/wiki-imageupload-plugins.css')}" rel="stylesheet">
	<link href="${url:resource('/stylesheets/jquery.autocomplete.css')}" rel="stylesheet">
</head>

<div class="section-qna">
	<slipp:header type="1"/>
	<div class="row-fluid">
		<div class="span9 qna-form">
			<c:set var="method" value="POST" />
			<c:if test="${not empty question.questionId}">
			<c:set var="method" value="PUT" />
			</c:if>
			<form:form modelAttribute="question" cssClass="form-horizontal" action="/questions" method="${method}">
				<form:hidden path="questionId"/>
				<fieldset>
					<div class="control-group">
						<form:input path="title" cssClass="input-block-level" placeholder="제목" />
					</div>
					<div class="control-group">
						<form:textarea path="contents" cols="80" rows="15"/>
					</div>
					<div class="control-group">
						<form:input path="plainTags" cssClass="input-block-level " placeholder="태그 - 공백 또는 쉼표로 구분 ex) javajigi, slipp" />
					</div>
					<c:if test="${loginUser.facebookUser and empty question.questionId}">
					<div class="control-group">
						내 페이스북으로 질문을 보내겠습니까?&nbsp;&nbsp;<form:checkbox path="connected" />
					</div>
					</c:if>
					<div class="pull-right">
						<button id="confirmBtn" type="submit" class="btn btn-success">질문하기</button>
					</div>
				</fieldset>
			</form:form>
		</div>
		<div class="span3 qna-side">
			<slipp:side-tags tags="${tags}"/>
		</div>
	</div>
</div>

<script src="http://ajax.microsoft.com/ajax/jquery.validate/1.7/jquery.validate.min.js"></script>
<script type="text/javascript" src="${url:resource('/javascripts/jquery.markitup.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/jquery.autocomplete.min.js')}"></script>
<script type="text/javascript"	src="${url:resource('/javascripts/qna/image.upload.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/qna/qna-set.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/qna/tagparser.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/qna/form.js')}"></script>
