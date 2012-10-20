<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
<link href="${url:resource('/stylesheets/wiki-style.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-textile-style.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-imageupload-plugins.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/jquery.autocomplete.css')}" rel="stylesheet">
</head>
<body>
	<slipp:header type="1"/>
	
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span10">
				<c:set var="method" value="POST" />
				<c:if test="${not empty question.questionId}">
				<c:set var="method" value="PUT" />
				</c:if>
				<form:form modelAttribute="question" cssClass="form-horizontal forumView" action="/questions" method="${method}">
					<form:hidden path="questionId"/>
					<fieldset>
						<div class="control-group">
							제목 : <form:input path="title" cssClass="input-xlarge focused span7"/>
						</div>
						<div class="control-group">
							<form:textarea path="contents" cols="80" rows="15"/>
						</div>
						<div class="control-group">
							태그 : <form:input path="plainTags" cssClass="input-xlarge focused span7"/><br/>
							태그 구분자로 공백 또는 쉼표(,)를 사용할 수 있습니다.
						</div>
						
						<div class="pull-right">
							<button id="confirmBtn" type="submit" class="btn btn-primary">질문하기</button>
							<a href="/questions" class="btn">목록보기</a>
						</div>
					</fieldset>				
				</form:form>
			</div>
			<slipp:side-tags tags="${tags}"/>
		</div>
	</div>
<script src="http://ajax.microsoft.com/ajax/jquery.validate/1.7/jquery.validate.min.js"></script>
<script type="text/javascript" src="${url:resource('/javascripts/jquery.markitup.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/jquery.autocomplete.min.js')}"></script>
<script type="text/javascript">
var uploaderUrl = "${slippUrl}";
</script>
<script type="text/javascript"	src="${url:resource('/javascripts/qna/image.upload.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/qna/qna-set.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/qna/tagparser.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/qna/form.js')}"></script>	
</body>
</html>