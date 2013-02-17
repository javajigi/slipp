<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
<title>${sf:h(question.title)}</title>
<link href="${url:resource('/stylesheets/wiki-style.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-textile-style.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-imageupload-plugins.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/sh/shCoreDefault.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/sh/shThemeEclipse.css')}" rel="stylesheet">
<meta property="og:type" content="article" />
<meta property="og:title" content="${sf:h(question.title)}" />
<meta property="og:site_name" content="SLiPP" />
<meta property="og:url" content="${slippUrl}/questions/${question.questionId}" /> 
<meta property="og:image" content="${slippUrl}${url:resource('/images/logo_slipp.png')}" />
</head>
<body>

<div class="section-qna">
	<slipp:header type="1"/>
	<div class="row-fluid">
		<div class="span9 qna-view">
			<slipp:show question="${question}"/>
		
			<div class="qna-comment">
				<p class="count"><strong>${question.answerCount}</strong>개의 답변</p>
				<ul class="list">
					<c:if test="${!empty question.bestAnswer}">
						<slipp:answer each="${question.bestAnswer}" isBest="true"/>
					</c:if>
					<c:forEach items="${question.answers}" var="each">
						<slipp:answer each="${each}" isBest="false"/>
					</c:forEach>
				</ul>
				<form id="deleteAnswerForm" action="/questions/${question.questionId}/answers/" method="POST" class="flyaway">
					<input type="hidden" name="_method" value="DELETE" />
				</form>
				<form id="likeAnswerForm" action="/questions/${question.questionId}/answers" method="POST" class="flyaway">
				</form>
				<sec:authorize access="!hasRole('ROLE_USER')">
					의견을 남기고 싶다면, <a href="/login" class="btn btn-primary btn-small">로그인</a>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_USER')">
					<form:form modelAttribute="answer" action="/questions/${question.questionId}/answers" method="POST" cssClass="form-horizontal">
						<fieldset>
							<form:textarea path="contents"  cols="80" rows="5"/>
							<c:if test="${loginUser.facebookUser}">
							내 페이스북으로 답변을 보내겠습니까?&nbsp;&nbsp;<form:checkbox path="connected" />
							</c:if>
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
</body>
</html>