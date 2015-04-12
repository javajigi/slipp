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
	<div class="content-main">
		<article class="article">
			<div class="article-header">
				<div class="article-header-thumb">
					<img src='${sf:stripHttp(question.writer.imageUrl)}' class="article-author-thumb" alt="" />
				</div>
				<div class="article-header-text">
					<a href="${sf:stripHttp(question.writer.url)}" class="article-author-name">${question.writer.userId}</a>
					<a href="/questions/${question.questionId}" class="article-header-time" title="퍼머링크">
						<fmt:formatDate value="${question.createdDate}" pattern="yyyy-MM-dd HH:mm" />
						<i class="icon-link"></i>
					</a>
				</div>
			</div>
			<div class="article-doc">
				${sf:wiki(question.contents)}
			</div>
			<div class="article-util">
				<ul class="article-util-list">
					<li>
						<a class="link-modify-article" href="/admin/questions/${question.questionId}/form?searchTerm=${searchTerm}">수정</a>
					</li>
					<li>
						<form class="form-delete" action="/admin/questions/${question.questionId}" method="POST">
							<input type="hidden" name="_method" value="DELETE" />
							<input type="hidden" name="searchTerm" value="${searchTerm}" />
							<button class="link-delete-article" type="submit">삭제</button>
						</form>
					</li>
				</ul>
			</div>
		</article>
		<div class="qna-comment">
			<div class="qna-comment-slipp">
				<p class="qna-comment-count"><strong>${question.answerCount}</strong>개의 의견 from SLiPP</p>
				<div class="qna-comment-slipp-articles">
					<div id="qna-recently-slipp-comment"></div>
					<c:forEach items="${question.answers}" var="each">
						<article class="article" id="answer-${each.answerId}">
							<div class="article-header">
								<div class="article-header-thumb">
									<img src='${sf:stripHttp(each.writer.imageUrl)}' class="article-author-thumb" alt="" />
								</div>
								<div class="article-header-text">
									<a href="${sf:stripHttp(each.writer.url)}" class="article-author-name">${each.writer.userId}</a>
									<a href="#answer-${each.answerId}" class="article-header-time" title="퍼머링크">
										<fmt:formatDate value="${each.createdDate}" pattern="yyyy-MM-dd HH:mm" />
										<i class="icon-link"></i>
									</a>
								</div>
							</div>
							<div class="article-doc comment-doc">
								${sf:wiki(each.contents)}
							</div>
							<div class="article-value">
							</div>
							<div class="article-util">
								<ul class="article-util-list">
									<li>
										<a class="link-modify-article" href="/admin/questions/${question.questionId}/answers/${each.answerId}/form?searchTerm=${searchTerm}">수정</a>
									</li>
								</ul>
							</div>
						</article>						
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
	<div class="content-sub">
		<div class="floating">
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
			<a class="link-back-to-list" href="/admin/questions?searchTerm=${searchTerm}">&larr; 목록으로</a>
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

