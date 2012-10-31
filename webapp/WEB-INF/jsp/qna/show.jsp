<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
<title>${sf:h(question.title)}</title>
<link href="${url:resource('/stylesheets/boards.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-style.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-textile-style.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-imageupload-plugins.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/sh/shCoreDefault.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/sh/shThemeEclipse.css')}" rel="stylesheet">

<style type="text/css">
.markItUpEditor { height:150px; }
</style>
</head>
<body>
	<slipp:header type="1"/>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span10">
				<div class="forumView">
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
			
				<div class="comment">
					<c:forEach items="${question.answers}" var="each">
					<div class="commentList">
						<div class="nickArea">
							<p class='prphoto'><img src='${sf:stripHttp(each.writer.imageUrl)}' /></p>
							<div class="nickname">
								<div class="tester"><span class='lv'><a href="${sf:stripHttp(each.writer.profileUrl)}">${each.writer.userId}</a></span></div>
							</div>
						</div>
						<div class="list">
							<div class="cont">${sf:wiki(each.contents)}</div>
							<div class="regDate"><fmt:formatDate value="${each.createdDate}" pattern="yyyy-MM-dd HH:mm" /></div>
						</div>
						<div class="commBtn" style="display: none;">
							<c:if test="${sf:isWriter(each.writer, loginUser)}">
							<a class="deleteAnswerBtn" data-answer-id="${each.answerId}" href="#">삭제</a>
							 | 
							</c:if>
							<sec:authorize access="hasRole('ROLE_USER')">
							<a class="recommentAnswerBtn" data-answer-id="${each.answerId}" data-answer-user-id="@${each.writer.userId}" href="#">댓글</a>
							</sec:authorize>
						</div>
					</div>
					</c:forEach>
					<form id="deleteAnswerForm" action="/questions/${question.questionId}/answers/" method="POST">
						<input type="hidden" name="_method" value="DELETE" />
					</form>					
				</div>
				<sec:authorize access="!hasRole('ROLE_USER')">
				<div class="pull-none">
					<p></p>
					<a href="/login"><input class="btn btn-primary" type="button" value="로그인" /></a>을 하면 이 글에 대한 답글을 쓸 수 있습니다.
				</div>	
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_USER')">
				<div class="form pull-none">
					<form:form modelAttribute="answer" action="/questions/${question.questionId}/answers" method="POST">
						<form:textarea path="contents"  cols="75" rows="5"/>
						<div class="button">
							<button id="answerBtn" type="submit" class="btn btn-primary pull-right">답변하기</button>
						</div>					
					</form:form>
				</div>				
				</sec:authorize>
			</div>			

			<slipp:side-tags tags="${tags}"/>
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