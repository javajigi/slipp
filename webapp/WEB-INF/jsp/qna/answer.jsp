<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>${sf:h(question.title)}</title>
	<link href="${url:resource('/stylesheets/wiki-style.css')}" rel="stylesheet">
	<link href="${url:resource('/stylesheets/wiki-textile-style.css')}" rel="stylesheet">
	<link href="${url:resource('/stylesheets/wiki-imageupload-plugins.css')}" rel="stylesheet">
	<link href="${url:resource('/stylesheets/sh/shCoreDefault.css')}" rel="stylesheet">
	<link href="${url:resource('/stylesheets/sh/shThemeEclipse.css')}" rel="stylesheet">
</head>

<div class="section-qna">
	<slipp:header type="1"/>
	<div class="row-fluid">
		<div class="span9 qna-view">
			<slipp:show question="${question}"/>

			<div class="qna-comment">
				<sec:authorize access="hasRole('ROLE_USER')">
					<form:form modelAttribute="answer" action="/questions/${question.questionId}/answers/${answer.answerId}" method="PUT" cssClass="form-horizontal">
						<fieldset>
							<form:textarea path="contents"  cols="80" rows="5"/>
							<div class="pull-right">
								<button id="answerBtn" type="submit" class="btn btn-success">답변하기</button>
							</div>
						</fieldset>
					</form:form>
				</sec:authorize>
			</div>
		</div>
		<div class="span3 qna-side">
			<slipp:side-tags tags="${tags}"/>
		</div>
	</div>
</div>
<script src="https://ajax.microsoft.com/ajax/jquery.validate/1.7/jquery.validate.min.js"></script>
<script type="text/javascript" src="${url:resource('/javascripts/jquery.markitup.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/sh/shCore.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/sh/shAutoloader.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/qna/syntaxhighlighter.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/qna/qna-set.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/qna/image.upload.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/qna/tagparser.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/qna/show.js')}"></script>
