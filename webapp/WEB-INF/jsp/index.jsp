<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/include/tags.jspf" %>
<div class="home-content">
	<div class="content-main">
		<section class="qna-list">
			<h1>최근화제</h1>
			<ul class="list">
			<c:forEach items="${questions.content}" end="4" var="each">
				<li>
					<div class="wrap">
						<div class="main">
							<strong class="subject">
								<a href="/questions/${each.questionId}">${sf:h(each.title)}</a>
							</strong>
							<c:if test="${each.denormalizedTags != ''}">
								<div class="tags">
									<i class="icon-tag" title="태그"></i>
									<span class="tag-list">
										<c:forEach items="${each.denormalizedTags}" var="tag">
											<span class="tag">${tag}</span>
										</c:forEach>
									</span>
								</div>
							</c:if>
							<div class="auth-info">
								<c:choose>
									<c:when test="${each.totalAnswerCount == 0}">
										<i class="icon-new-article"></i>
										<span class="type">새글</span>
										<span class="time">
											<fmt:formatDate value="${each.createdDate}" pattern="yyyy-MM-dd HH:mm" />
										</span>
										<a href="${each.latestParticipant.url}" class="author">${each.latestParticipant.userId}</a>
									</c:when>
									<c:otherwise>
										<i class="icon-add-comment"></i>
										<span class="type">응답</span>
										<span class="time">
											<fmt:formatDate value="${each.updatedDate}" pattern="yyyy-MM-dd HH:mm" />
										</span>
										<a href="${each.latestParticipant.url}" class="author">${each.latestParticipant.userId}</a>
									</c:otherwise>
								</c:choose>
							</div>
							<div class="reply" title="댓글">
								<i class="icon-reply"></i>
								<span class="point">${each.totalAnswerCount}</span>
							</div>
						</div>
					</div>
				</li>
			</c:forEach>
			</ul>
			<nav class="link-more">
				<a href="/questions">전체목록보기 &raquo;</a>
			</nav>
		</section>
		<slipp:side-tags tags="${tags}"/>
	</div>
	<div class="content-sub">
		<section class="smalltalk ui-smalltalk-list-collapse">
			<h1>수다양!</h1>
			<sec:authorize access="hasRole('ROLE_USER')">
				<form action="" class="smalltalk-form">
						<textarea id="smallTalkMessage" name="smallTalkMessage" class="tf-smalltalk-form-msg" style="resize: none"></textarea>
						<div class="smalltalk-form-util">
							<p class="smalltalk-form-util-msg"><i class="icon-smalltalk-msg"></i> 요즘 어떠세요?</p>
							<p class="smalltalk-form-fail-msg" style="display: none"></p>
							<button type="submit " class="btn-smalltalk-form-util-submit">나도 한마디</button>
						</div>
				</form>
			</sec:authorize>
			<ul class="smalltalk-list">
				<c:forEach items="${smallTalks}" var="smallTalk" varStatus="status">
				<li class="smalltalk-list-item smalltalk-list-item-${status.count}">
					<div class="smalltalk-list-item-info">
						<strong class="smalltalk-list-item-info-author">${smallTalk.writer.userId}</strong>
						<span class="smalltalk-list-item-info-time">${smallTalk.time}</span>
					</div>
					<div class="smalltalk-list-item-cont">${smallTalk.talk}</div>
				</li>
				</c:forEach>
				<li class="smalltalk-list-expand">
					<button class="btn-smalltalk-list-expand" data-smalltalk-count="${fn:length(smallTalks)}">more <i class="icon-smalltalk-expand"></i></button>
				</li>
			</ul>
			<p class="smalltalk-notice">* 최근 10개까지만 보여집니다.</p>
		</section>
		<section class="notice">
			<h1><a href="/wiki/display/slipp/Home">SLiPP log</a></h1>
			<ul class="notice-list">
				<c:forEach items="${pages}" var="page" varStatus="status" end="2">
				<li class="notice-list-item">
					<strong class="notice-item-title"><a href="/wiki/pages/viewpage.action?pageId=${page.pageId}">${page.title}</a></strong>
					<div class="notice-list-item-time">${page.creationDate}</div>
					<c:if test="${status.index == 0}">
					<div class="notice-list-item-cont">
						${page.shortContents}
					</div>
					</c:if>
				</li>
				</c:forEach>
			</ul>
			<div class="rss">
				<a href="http://feeds.feedburner.com/slipp"><img src="http://feeds.feedburner.com/~fc/slipp?bg=99CCFF&amp;fg=444444&amp;anim=0" height="26" width="88" style="border:0" alt="" /></a>
			</div>
		</section>
	</div>
</div>
<script src="${url:resource('/javascripts/jquery.tmpl.min.js')}"></script>
<script src="${url:resource('/javascripts/main/smalltalk.js')}"></script>
<script type="text/javascript">
	$(document).ready(function(){
		smalltalkService.init();
	});
</script>
<script type="text/x-tmpl" id="tmpl-smalltalk-list">
{% for (var i=0; i<o.length; i++) { %}
	<li class="smalltalk-list-item smalltalk-list-item-${status.count}">
		<div class="smalltalk-list-item-info">
			<strong class="smalltalk-list-item-info-author">{%=o[i].writer.userId%}</strong>
			<span class="smalltalk-list-item-info-time">{%=o[i].time%}</span>
		</div>
		<div class="smalltalk-list-item-cont">{%=o[i].talk%}</div>
	</li>
{% } %}
</script>