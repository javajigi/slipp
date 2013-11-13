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
			<div class="qna-comment-slipp">
				<c:if test="${!empty question.bestAnswer}">
					<slipp:answer each="${question.bestAnswer}" isBest="true"/>
				</c:if>
				<p class="article-count"><strong>${question.answerCount}</strong>개의 의견 from SLiPP</p>
				<div class="qna-comment-slipp-articles">
					<c:forEach items="${question.answers}" var="each">
						<slipp:answer each="${each}" isBest="false"/>
					</c:forEach>
				</div>
			</div>
			<form:form modelAttribute="answer" action="/questions/${question.questionId}/answers" method="POST" cssClass="form-write">
				<fieldset>
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
						</sec:authorize>
						<button type="submit" class="btn-submit"><i class="icon-submit"></i> 작성완료</button>
					</div>
				</fieldset>
			</form:form>
			<sec:authorize access="!hasRole('ROLE_USER')">
	        <div class="choose-login-type">
	            <div class="login-with-slipp">
	                <p class="login-with-slipp-text">SLiPP 계정으로 로그인하세요.</p>
	                <form id="authentication" action="/users/authenticate" method="POST">
	                    <input id="authenticationId" name="authenticationId" class="inp-email" placeholder="이메일" type="text" value=""/>
	                    <input id="authenticationPassword"  name="authenticationPassword" class="inp-title" placeholder="비밀번호" type="password" value=""/>
	                    <c:if test="${not empty param.login_error}">
	                    <label class="error" style="">메일주소 혹은 비밀번호가 정확하지 않습니다.</label>
	                    </c:if>
	                    <p class="login-with-slipp-submit">
	                        <label><input name="_spring_security_remember_me" class="inp-pw" type="checkbox" value="true"/> 자동로그인</label>
	                        <button id="loginSubmitBtn" type="submit" class="login-with-slipp-submit-btn"><i class="icon-login"></i> 로그인</button>
	                    </p>
	                </form>
	            </div>
	            <div class="divide-bar left"></div>
	            <div class="login-with-sns">
	                <p class="login-with-sns-text">또는, SNS 계정으로 로그인하세요.</p>
	                <form action="/signin/facebook" method="POST">
	                    <input type="hidden" name="scope" value="publish_stream,user_groups" />
	                    <input type="hidden" name="redirect" value="/questions/${question.questionId}" />
	                    <button type="submit" class="btn-login-facebook"><i class="foundicon-facebook"></i> 페이스북</button>
	                </form>
	                <form action="/signin/twitter" method="POST">
	                    <button type="submit" class="btn-login-twitter"><i class="foundicon-twitter"></i> 트위터</button>
	                </form>
	                <form action="/signin/google?scope=https://www.googleapis.com/auth/userinfo.profile" method="POST">
	                    <button type="submit" class="btn-login-google"><i class="foundicon-google-plus"></i> 구글</button>
	                </form>
	            </div>
	            <div class="divide-bar right"></div>
	            <div class="sign-in-to-slipp">
	                <p class="sign-in-to-slipp-text">계정이 없다면 간단히 만들어보세요.</p>
	                <form:form modelAttribute="user" cssClass="form-write" action="/users" method="post">
	                        <form:input path="email" class="inp_email" placeholder="이메일" />
	                        <form:input path="userId" class="inp_pw" placeholder="닉네임" />
	                        <p class="sign-in-to-slipp-notice">
	                            - 등록한 메일로 임시 비밀번호를 보내드립니다. <br />
	                            - 개인공간에서 비밀번호를 변경할 수 있습니다.</p>
	                        <button type="submit" class="sign-in-to-slipp-btn"><i class="icon-signin"></i> 회원가입</button>
	                </form:form>
	            </div>
	        </div>
			</sec:authorize>
			<c:if test="${question.snsConnected}">
				<div class="qna-comment-fb">
					<p class="article-count">
						<strong>${question.snsAnswerCount}</strong>개의 의견 from FB
					</p>
					<div class="qna-comment-fb-articles">
						<div class="qna-facebook-comment"></div>
					</div>
				</div>
			</c:if>
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
				$('.qna-facebook-comment').html(response);
				return false;
			}, 'html'
		);
	}

	showFacebookComments(${question.questionId});
});
$('#contents').markItUp(mySettings);
</script>

