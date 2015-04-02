<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>${sf:h(question.title)}</title>
	<link rel="stylesheet" href="${url:resource('/stylesheets/highlight/magula.css')}">
	<link rel="stylesheet" href="${url:resource('/stylesheets/wiki-style.css')}">
	<link rel="stylesheet" href="${url:resource('/stylesheets/wiki-textile-style.css')}">
</head>

<section class="view-content">
	<h1 class="article-title">${sf:h(question.title)}</h1>
	<div class="content-main">
		<slipp:show question="${question}"/>
		<div class="qna-comment">
			<sec:authorize access="hasRole('ROLE_USER')">
				<form:form modelAttribute="answer" action="/admin/questions/${question.questionId}/answers/${answer.answerId}" method="PUT" cssClass="form-write">
					<input type="hidden" name="searchTerm" value="${searchTerm}" />
					<fieldset>
						<legend class="title-write">의견 추가하기</legend>
						<div class="box-write">
							<form:textarea path="contents"  cols="80" rows="15"/>
						</div>
						<div class="submit-write">
							<button type="submit" class="btn-submit"><i class="icon-submit"></i> 작성완료</button>
						</div>
					</fieldset>
				</form:form>
			</sec:authorize>
		</div>
	</div>
</section>
<script src="${url:resource('/javascripts/jquery.validate.min.js')}"></script>
<script src="${url:resource('/javascripts/highlight.pack.js')}"></script>
<script>
hljs.initHighlightingOnLoad();
</script>
<script src="${url:resource('/javascripts/qna/tagparser.js')}"></script>
<script src="${url:resource('/javascripts/qna/answer.js')}"></script>
<script src="${url:resource('/javascripts/qna/write.js')}"></script>
<script src="${url:resource('/javascripts/qna/qna-set.js')}"></script>
<script src="${url:resource('/javascripts/qna/image.upload.js')}"></script>
<script src="${url:resource('/javascripts/jquery.markitup.js')}"></script>
<script>
$('#contents').markItUp(mySettings);
</script>