<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
<link href="${url:resource('/stylesheets/boards.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-style.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-textile-style.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-imageupload-plugins.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/jquery.autocomplete.css')}" rel="stylesheet">
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span10">
				<form:form modelAttribute="question" cssClass="form-horizontal" action="/questions" method="POST">
					<fieldset>
						<legend>SLiPP Q&A 질문하기</legend>
						<div class="control-group">
							<label class="control-label" for="title">제목</label>
							<div class="controls">
								<form:input path="title" cssClass="input-xlarge focused span7"/>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">내용</label>
							<div class="controls">
								<form:textarea path="contents" cssClass="input-xlarge span7" cols="75" rows="15"/>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">태그</label>
							<div class="controls">
								<form:input path="plainTags" cssClass="input-xlarge focused span7"/>
							</div>
						</div>
						
						<div class="form-actions">
							<button type="submit" class="btn btn-primary">질문하기</button>
							<a href="/questions"><button class="btn">목록보기</button></a>
						</div>
					</fieldset>				
				</form:form>
			</div>
			<div class="span2">
				<div class="tags">
					<ul>
						<li>java</li>
						<li>java1</li>
						<li>java2</li>
						<li>java3</li>
						<li>java4</li>
						<li>java5</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
<script src="http://ajax.microsoft.com/ajax/jquery.validate/1.7/jquery.validate.min.js"></script>
<script type="text/javascript" src="${url:resource('/javascripts/jquery.markitup.js')}"></script>
<script type="text/javascript">
var uploaderUrl = "http://localhost:8080";
</script>
<script type="text/javascript"	src="${url:resource('/javascripts/qna/image.upload.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/qna/qna-set.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/qna/tagparser.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/qna/form.js')}"></script>	
</body>
</html>