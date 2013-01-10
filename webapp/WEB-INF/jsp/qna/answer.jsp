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
</head>
<body>

<div class="section-qna">
	<slipp:header type="1"/>
	<div class="row-fluid">
		<div class="span9 qna-view">
			<div class="content">
				<article class="article">
					<div class="wrap">
						<div class="auth-info">
							<div class="author-thumb">
								<img src='${sf:stripHttp(question.writer.imageUrl)}' class="user-thumb" alt="" />
							</div>
							<div class="author-text">
								<a href="${sf:stripHttp(question.writer.profileUrl)}" class="author-name">${question.writer.userId}</a>
								<span class="time">
									<fmt:formatDate value="${question.createdDate}" pattern="yyyy-MM-dd HH:mm" />
								</span>
							</div>
						</div>
						<div class="doc">
							<h1 class="subject">${sf:h(question.title)}</h1>
							<div class="tags">
								<ul>
								<c:forEach items="${question.tags}" var="tag">
									<li>
										<a href="/questions/tagged/${tag.name}" class="tag">${tag.name}</a>
									</li>
								</c:forEach>
								</ul>
							</div>
							<div class="text">${sf:wiki(question.contents)}</div>
							<div class="share">
								<div class="facebook sns">
									<div id="fb-root"></div>
									<script src="https://connect.facebook.net/en_US/all.js#xfbml=1"></script>
									<fb:like href="${slippUrl}/questions/${question.questionId}"
									git	send="true" layout="button_count" width="100" show_faces="true"
										font=""></fb:like>
								</div>
								<div class="googleplus sns">
									<g:plusone></g:plusone>
								</div>
								<div class="twitter sns">
									<a href="https://twitter.com/share" class="twitter-share-button"
										data-count="horizontal">Tweet</a>
									<script type="text/javascript"
										src="https://platform.twitter.com/widgets.js"></script>
								</div>
							</div>
							<div class="util">
								<c:if test="${sf:isWriter(question.writer, loginUser)}">
								<a href="/questions/${question.questionId}/form" class="btn btn-primary">수정</a>
								<a id="deleteQuestionBtn" href="#" class="btn btn-danger">삭제</a>
								</c:if>	
								<a href="/questions"><button class="btn">목록으로</button></a>
							</div>
						</div>
					</div>
				</article>
				<form id="deleteQuestionForm" action="/questions/${question.questionId}" method="POST" class="flyaway">
					<input type="hidden" name="_method" value="DELETE" />
				</form>
			</div>
		
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