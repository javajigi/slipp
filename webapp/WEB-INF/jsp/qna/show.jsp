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
	<header class="qna-header">
		<h1 class="qna-title">${sf:h(question.title)}</h1>
		<div class="qna-nav">
			<a href="#qna-comment-form">의견 추가하기&darr;</a>
			<span class="divider"> / </span>
			<a href="#qna-recently-slipp-comment">SLiPP 최신의견&darr;</a>
			<c:if test="${question.snsConnected}">
			<span class="divider"> / </span>
			<a href="#qna-recently-fb-comment">페북 최신의견&darr;</a>
			</c:if>
		</div>
	</header>
	<div class="content-main">
		<slipp:show question="${question}"/>
		<div class="qna-comment">
			<c:if test="${question.snsConnected}">
				<div class="qna-comment-fb">
					<p class="qna-comment-count">
						<strong>${question.snsAnswerCount}</strong>개의 의견 from FB
					</p>
					<div class="qna-comment-fb-articles">
						<div id="qna-recently-fb-comment"></div>
						<div class="qna-facebook-comment"></div>
					</div>
				</div>
			</c:if>
			<div class="qna-comment-slipp">
				<c:if test="${!empty question.bestAnswer}">
					<slipp:answer each="${question.bestAnswer}" isBest="true"/>
				</c:if>
				<p class="qna-comment-count"><strong>${question.answerCount}</strong>개의 의견 from SLiPP</p>
				<div class="qna-comment-slipp-articles">
					<div id="qna-recently-slipp-comment"></div>
					<c:forEach items="${question.answers}" var="each">
						<slipp:answer each="${each}" isBest="false"/>
					</c:forEach>
				</div>
			</div>
			<script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
			<ins class="adsbygoogle" style="display:block;width:728px;height:90px;margin-top:20px;margin-bottom:20px;" data-ad-client="ca-pub-0116131400397176" data-ad-slot="3364168137"></ins>
			<script>(adsbygoogle = window.adsbygoogle || []).push({});</script>
			<form:form modelAttribute="answer" action="/questions/${question.questionId}/answers" method="POST" cssClass="form-write">
				<fieldset id="qna-comment-form">
					<legend class="title-write">의견 추가하기</legend>
					<div class="box-write">
						<form:textarea path="contents" cols="80" rows="15"/>
					</div>
					<div class="submit-write">
						<sec:authorize access="hasRole('ROLE_USER')">
						<c:if test="${loginUser.facebookUser}">
						<label class="msg-send-to-facebook">
							<form:checkbox path="connected" /> 페이스북으로 전송하려면 체크하세요
						</label>
						</c:if>
						<button type="submit" class="btn-submit"><i class="icon-submit"></i> 작성완료</button>
						</sec:authorize>
					</div>
				</fieldset>
			</form:form>
			<sec:authorize access="!hasRole('ROLE_USER')">
				<div class="qna-login-for-comment">
					<p class="qna-login-for-comment-msg">의견을 남기기 위해서는 SLiPP 계정이 필요합니다.<br />
					안심하세요! <b>회원가입/로그인 후에도 작성하시던 내용은 안전하게 보존됩니다.</b></p>
					<slipp:loginform redirectUrl="/questions/${question.questionId}"/>
				</div>
			</sec:authorize>
		</div>
	</div>
	<div class="content-sub">
		<div class="floating">
			<%--
			<section class="like-question">
				<h1>관심두기</h1>
				<a class="btn-like-question" href="/api/questions/${question.questionId}/like" title="관심글 등록">
					<i class="icon-like-question"></i>
					<strong class="like-question-count">${question.sumLike}</strong>
					<span class="like-question-txt">관심글</span>
				</a>
			</section>
			--%>
			<section class="share">
				<h1>SNS공유</h1>
				<div class="share-facebook">
					<div id="fb-root"></div>
					<script src="https://connect.facebook.net/en_US/all.js#xfbml=1"></script>
					<fb:like href="${facebookSlippUrl}/questions/${question.questionId}"
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
				<c:forEach items="${question.tags}" var="tag">
					<li>
						<a href="/questions/tagged/${tag.name}" class="tag">${tag.name}</a>
						<sec:authorize access="hasRole('ROLE_USER')">
						&nbsp;<a class="link-detagged" href="/api/questions/${question.questionId}/detagged/${tag.name}">X</a>
						</sec:authorize>
					</li>
				</c:forEach>
				</ul>
			</section>
			<sec:authorize access="hasRole('ROLE_USER')">
			<form id="taggedForm" action="/questions/${question.questionId}/tagged" method="post" cssClass="signin-with-sns">
				<input type="text" name="taggedName" class="inp_nickname focused"><br/>
				<button class="signin-with-sns-submit-btn" type="submit">태그추가</button>
			</form><br/>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_ADMINISTRATOR')">
			<form id="connectedFBForm" action="/questions/${question.questionId}/connect/facebook" method="post" cssClass="signin-with-sns">
				<input type="text" name="fbPostId" class="inp_nickname focused"><br/>
				<button class="signin-with-sns-submit-btn" type="submit">페북글 연결</button>
			</form><br/>
			</sec:authorize>
			<a class="link-back-to-list" href="/questions">&larr; 목록으로</a>
		</div>
	</div>
</section>

<script src="https://apis.google.com/js/plusone.js"></script>
<script src="${url:resource('/javascripts/jquery.validate.min.js')}"></script>
<script src="${url:resource('/javascripts/highlight.pack.js')}"></script>
<script>
hljs.initHighlightingOnLoad();
questionId = ${question.questionId};
guestUser = ${loginUser.guest};
</script>
<script src="${url:resource('/javascripts/qna/tagparser.js')}"></script>
<script src="${url:resource('/javascripts/qna/show.js')}"></script>
<script src="${url:resource('/javascripts/qna/write.js')}"></script>
<script src="${url:resource('/javascripts/qna/qna-set.js')}"></script>
<script src="${url:resource('/javascripts/qna/image.upload.js')}"></script>
<script src="${url:resource('/javascripts/jquery.markitup.js')}"></script>
<script src="${url:resource('/javascripts/support/slipp.validate.js')}"></script>
<script src="${url:resource('/javascripts/user/login.js')}"></script>
<sec:authorize access="!hasRole('ROLE_USER')">
<script src="${url:resource('/javascripts/qna/loginForm.js')}"></script>
</sec:authorize>
<script>
$('#contents').markItUp(mySettings);
</script>

