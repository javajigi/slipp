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
		<section class="smalltalks">
			<h1>수다양!</h1>
			<ul class="smalltalks-list">
				<c:forEach items="${smallTalks}" var="smallTalk">
				<li>
					<strong class="title"><pre>${smallTalk.talk}</pre></strong>
					<div class="time">${smallTalk.time}</div>
					<div class="writer">${smallTalk.writer.userId}</div>
				</li>
				</c:forEach>
			</ul>
			<sec:authorize access="hasRole('ROLE_USER')">
				<form action="" class="smalltalks-form">
					<div style="float: left;">
						<textarea id="smallTalkMessage" name="smallTalkMessage"></textarea>
					</div>
					<div style="float: left;">
						<button type="submit " class="btn-smalltalks-form-submit">[저장하기]</button>
					</div>
				</form>
			</sec:authorize>
		</section>
		<section class="notice">
			<h1><a href="/wiki/display/slipp/Home">SLiPP log</a></h1>
			<ul class="list">
				<c:forEach items="${pages}" var="page">
				<li>
					<strong class="title"><a href="/wiki/pages/viewpage.action?pageId=${page.pageId}">${page.title}</a></strong>
					<div class="time">${page.creationDate}</div>
					<div class="cont">
						${page.shortContents}
					</div>
				</li>
				</c:forEach>
			</ul>
			<div class="rss">
				<a href="http://feeds.feedburner.com/slipp"><img src="http://feeds.feedburner.com/~fc/slipp?bg=99CCFF&amp;fg=444444&amp;anim=0" height="26" width="88" style="border:0" alt="" /></a>
			</div>
		</section>
	</div>
</div>
<%-- 이동 예정. --%>
<script src="${url:resource('/javascripts/jquery.tmpl.min.js')}"></script>
<script src="${url:resource('/javascripts/autogrow.min.js')}"></script>
<script type="text/x-tmpl" id="tmpl-smalltalks-list">
{% for (var i=0; i<o.length; i++) { %}
	<li>
		<strong class="title"><pre>{%=o[i].talk%}</pre></strong>
		<div class="time">{%=o[i].time%}</div>
	</li>
{% } %}
</script>
<script type="text/javascript">
	var smallTalkService = {
		init: function() {
			var that = this;

			$(that.MessageField).autogrow();
			$('.smalltalks-form').on('submit', function(evt){
				evt.preventDefault();
				that.save();
			});
		},
		MessageField: '#smallTalkMessage',
		save : function(){
			var that = this;
			var $talk = $(that.MessageField);

			$.post('/smalltalks', { 'talk' : $talk.val() }, function(data) {
				if( data == 'OK' ){
					that.get();
					$talk.val('');
				}
			});
		},
		get : function(){
			$.get('/smalltalks', function(data) {
				$('.smalltalks-list').html( tmpl('tmpl-smalltalks-list', data) );
			});
		}
	};

	$(document).ready(function(){
		smallTalkService.init();
	});
</script>
