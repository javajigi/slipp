<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
<title>${sf:h(question.title)}</title>
<link href="${url:resource('/stylesheets/slipp.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-style.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-textile-style.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-imageupload-plugins.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/sh/shCoreDefault.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/sh/shThemeEclipse.css')}" rel="stylesheet">

</head>
<body>

<div class="section-qna">
	<slipp:header type="1"/>
	<div class="row-fluid">
		<div class="span9 qna-view">
			<div class="content">
				<div class="nickArea">
					<p class='nick'>
						<img src='${sf:stripHttp(question.writer.imageUrl)}' width="50" height="50" />&nbsp;&nbsp;
						<a href="${sf:stripHttp(question.writer.profileUrl)}">${question.writer.userId}</a></p>
					<p class="regDate"><fmt:formatDate value="${question.createdDate}" pattern="yyyy-MM-dd HH:mm" /></p>
				</div>
				<div class="contents">
					<strong class="subject">${sf:h(question.title)}</strong>
					<div>${sf:wiki(question.contents)}</div>
				</div>
				<div class="follow">
					<p class="tags">
						<c:forEach items="${question.tags}" var="tag">
						<a href="/questions/tagged/${tag.name}"><strong>${tag.name}</strong></a>	
						</c:forEach> 
					</p>
					<p class="count">
						<span class="answerNum">답변수 <strong>${question.answerCount}</strong></span>
					</p>
				</div>

				<div class="snsIcon">
					<div class="facebook">
						<div id="fb-root"></div>
						<script src="https://connect.facebook.net/en_US/all.js#xfbml=1"></script>
						<fb:like href="${slippUrl}/questions/${question.questionId}"
							send="true" layout="button_count" width="100" show_faces="true"
							font=""></fb:like>
					</div>
					<div class="googleplus">
						<g:plusone></g:plusone>
					</div>
					<div class="twitter">
						<a href="https://twitter.com/share" class="twitter-share-button"
							data-count="horizontal">Tweet</a>
						<script type="text/javascript"
							src="https://platform.twitter.com/widgets.js"></script>
					</div>
				</div>
				<div class="button-qna">
					<c:if test="${sf:isWriter(question.writer, loginUser)}">
					<a href="/questions/${question.questionId}/form"><button class="btn btn-primary">수정하기</button></a>
					<a id="deleteQuestionBtn" href="#"><button class="btn btn-primary">삭제하기</button></a>
					</c:if>
					<a href="/questions"><button class="btn pull-right">목록으로</button></a>				
				</div>
				<form id="deleteQuestionForm" action="/questions/${question.questionId}" method="POST">
					<input type="hidden" name="_method" value="DELETE" />
				</form>					
			</div>
		
			<div class="qna-comment">
				<ul class="list">
					<c:if test="${!empty question.bestAnswer}">
						<c:set var="each" value="${question.bestAnswer}"/>
						<slipp:answer each="${each}" isBest="true"/>
					</c:if>
					<c:forEach items="${question.answers}" var="each">
						<slipp:answer each="${each}" isBest="false"/>
					</c:forEach>
				</ul>
				<form id="deleteAnswerForm" action="/questions/${question.questionId}/answers/" method="POST">
					<input type="hidden" name="_method" value="DELETE" />
				</form>					
				<form id="likeAnswerForm" action="/questions/${question.questionId}/answers/" method="POST">
				</form>		
				<sec:authorize access="!hasRole('ROLE_USER')">
					의견을 남기고 싶다면, <a href="/login" class="btn btn-primary btn-small">로그인</a>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_USER')">
					<form:form modelAttribute="answer" action="/questions/${question.questionId}/answers" method="POST" cssClass="form-horizontal">
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
</body>
</html>