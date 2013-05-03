<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>${sf:h(question.title)}</title>
	<meta property="og:type" content="article" />
	<meta property="og:title" content="${sf:h(question.title)}" />
	<meta property="og:site_name" content="SLiPP" />
	<meta property="og:url" content="${slippUrl}/questions/${question.questionId}" />
	<meta property="og:image" content="${slippUrl}${url:resource('/images/logo_slipp.png')}" />
	<link rel="stylesheet" href="${url:resource('/stylesheets/highlight/github.css')}">
	<link rel="stylesheet" href="${url:resource('/stylesheets/wiki-style.css')}">
	<link rel="stylesheet" href="${url:resource('/stylesheets/wiki-textile-style.css')}">
</head>


<section class="view-content">
	<h1 class="article-title">${sf:h(question.title)}</h1>
	<div class="content-main">
		<slipp:show question="${question}"/>
		<div class="qna-comment">
			<c:if test="${!empty question.bestAnswer}">
				<slipp:answer each="${question.bestAnswer}" isBest="true"/>
			</c:if>
			<p class="article-count"><strong>${question.answerCount}</strong>개의 의견</p>
			<c:forEach items="${question.answers}" var="each">
				<slipp:answer each="${each}" isBest="false"/>
			</c:forEach>
			</form>
			
			페이스북 댓글
			<div class="qna-facebook-comment">
			</div>
			
			<sec:authorize access="!hasRole('ROLE_USER')">
				<p class="msg-to-login">
					<b><a href="/login">로그인</a></b>해서 의견을 나누세요!
				</p>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_USER')">
				<form:form modelAttribute="answer" action="/questions/${question.questionId}/answers" method="POST" cssClass="form-write">
					<fieldset>
						<legend class="title-write">의견 추가하기</legend>
						<div class="box-write">
							<form:textarea path="contents"  cols="80" rows="15"/>
						</div>
						<div class="submit-write">
							<c:if test="${loginUser.facebookUser}">
							<label class="msg-send-to-facebook">
								<form:checkbox path="connected" /> 페이스북으로 전송
							</label>
							</c:if>
							<button type="submit" class="btn-submit"><i class="icon-submit"></i> 작성완료</button>
						</div>
					</fieldset>
				</form:form>
			</sec:authorize>
		</div>
	</div>
	<div class="content-sub">
		<div class="floating">
			<section class="share">
				<h1>SNS공유</h1>
				<div class="share-facebook">
					<div id="fb-root"></div>
					<script src="https://connect.facebook.net/en_US/all.js#xfbml=1"></script>
					<fb:like href="${slippUrl}/questions/${question.questionId}"
					git	send="true" layout="button_count" width="100" show_faces="true" font=""></fb:like>
				</div>
				<div class="share-googleplus">
					<g:plusone></g:plusone>
				</div>
				<div class="share-twitter">
					<a href="https://twitter.com/share" class="twitter-share-button"
						data-count="horizontal">Tweet</a>
					<script type="text/javascript" src="https://platform.twitter.com/widgets.js"></script>
				</div>
			</section>
			<section class="qna-tags">
				<h1>연관태그</h1>
				<ul>
				<c:forEach items="${question.pooledTags}" var="tag">
					<li>
						<a href="/questions/tagged/${tag.name}" class="tag">${tag.name}</a>
					</li>
				</c:forEach>
				</ul>
			</section>
			<a class="link-back-to-list" href="/questions">&larr; 목록으로</a>
		</div>
	</div>
</section>

<script src="${url:resource('/javascripts/jquery.validate.min.js')}"></script>
<script src="${url:resource('/javascripts/highlight.pack.js')}"></script>
<script>
hljs.initHighlightingOnLoad();
</script>
<script src="${url:resource('/javascripts/qna/tagparser.js')}"></script>
<script src="${url:resource('/javascripts/qna/show.js')}"></script>
<script src="${url:resource('/javascripts/qna/write.js')}"></script>
<script src="${url:resource('/javascripts/qna/qna-set.js')}"></script>
<script src="${url:resource('/javascripts/qna/image.upload.js')}"></script>
<script src="${url:resource('/javascripts/jquery.markitup.js')}"></script>
<script>
$(document).ready(function(){
	function showFacebookComments(questionId) {
		var url = '/api/facebooks/' + questionId + '/comments';
		$.get(url,
			function(response) {
				console.log(response);
				$('.qna-facebook-comment').html(response);
				return false;
			}, 'html'
		);
	}	
	
	showFacebookComments(${question.questionId});
});
$('#contents').markItUp(mySettings);
</script>

