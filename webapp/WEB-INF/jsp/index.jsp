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
		<section class="notice">
			<h1><a href="">수다양!</a></h1>
			<ul class="list smallTalksUl">
				<c:forEach items="${smallTalks}" var="smallTalk">
				<li>
					<strong class="title"><pre>${smallTalk.talk}</pre></strong>
					<div class="time">${smallTalk.time}</div>
					<div class="writer">${smallTalk.writer.userId}</div>
				</li>
				</c:forEach>
			</ul>
			<sec:authorize access="hasRole('ROLE_USER')">
			<div style="float: left;">
				<textarea id="smallTalk" name="smallTalk"></textarea>
			</div>
			<div style="float: left;">
				<a href="#" class="submitBtn">[저장하기]</a>
			</div>
			</sec:authorize>
		</section>
		<section class="notice">
			<h1><a href="/wiki/display/slipp/Home">SLiPP log</a></h1>
			<ul class="list">
				
				<li>
					<strong class="title"><a href="/wiki/pages/viewpage.action?pageId=14549250">나는 가르칠 수 있는 용기를 가지고 있는가?</a></strong>
					<div class="time">2013-06-29 23:09:03.0</div>
					<div class="cont">
						요즘 2학기 수업 준비하는데 온 신경을 집중하다보니 책을 읽어도 책 내용이 잘 읽히지 않고, 집중이 잘 안된다. 이 책을 읽는데도 2주 이상의 시간이 흘렀다. 그 만큼 다른 것에 정신이 팔려있는 요즘이다. 더딘 속도이기는 하지만 그래도 책을 끝까지 읽었다. 최근에는 책...
					</div>
				</li>
				
				<li>
					<strong class="title"><a href="/wiki/pages/viewpage.action?pageId=14549188">2013학년 1학기 소프트웨어 공학 수업을 마치면서...</a></strong>
					<div class="time">2013-06-20 19:16:11.0</div>
					<div class="cont">
						소프트웨어 공학 첫번째 수업소프트웨어 공학 서로 알아가기 및 주제 선정 모임소프트웨어 공학 두번째 수업소프트웨어 공학 세번째 수업 - 요구사항 분석소프트웨어 공학 네번째 수업 - 요구사항 분석2소프트웨어 공학 다섯번째 수업 - 우선순위 선정 및 일정 추정소프트웨어 공학...
					</div>
				</li>
				
				<li>
					<strong class="title"><a href="/wiki/pages/viewpage.action?pageId=14549052">facebook graph api 사용을 위한 권한 처리</a></strong>
					<div class="time">2013-06-12 12:03:16.0</div>
					<div class="cont">
						 글에서도 언급했지만 오늘 또 한번의 삽질을 해서 다시 한번 글을 남긴다. 오늘 삽질한 내용은 현재 내가 속해 있는 group 목록을 가져오는 작업이었다. facebook에 &quot;/me/groups&quot;를 활용해 group 목록을 가져올 수 있다. 그런데 이...
					</div>
				</li>
				
				<li>
					<strong class="title"><a href="/wiki/pages/viewpage.action?pageId=14548997">좋은 프로그래머가 되려면 검색을 적극 활용해라.</a></strong>
					<div class="time">2013-06-05 21:51:47.0</div>
					<div class="cont">
						어제 학생과 면담을 하다 느낀 점이 있어 이 느낌이 사라지기 전에 간단하게 글로 남겨보려 한다.학생과 면담을 하는데 다음과 같은 이야기를 들었다.고등학교에서 대학교로 왔을 때 가장 낯설었던 경험이 프로그래밍 문제를 해결하는데 지금까지 배운 지식을 바탕으로 문제를 해결하...
					</div>
				</li>
				
				<li>
					<strong class="title"><a href="/wiki/pages/viewpage.action?pageId=12189776">소프트웨어 공학 일곱번째 수업 - 테스트</a></strong>
					<div class="time">2013-04-19 12:07:48.0</div>
					<div class="cont">
						소프트웨어 공학 첫번째 수업소프트웨어 공학 서로 알아가기 및 주제 선정 모임소프트웨어 공학 두번째 수업소프트웨어 공학 세번째 수업 - 요구사항 분석소프트웨어 공학 네번째 수업 - 요구사항 분석2소프트웨어 공학 다섯번째 수업 - 우선순위 선정 및 일정 추정이번 주는 특별...
					</div>
				</li>
				
			</ul>
			<div class="rss">
				<a href="http://feeds.feedburner.com/slipp"><img src="http://feeds.feedburner.com/~fc/slipp?bg=99CCFF&amp;fg=444444&amp;anim=0" height="26" width="88" style="border:0" alt="" /></a>
			</div>
		</section>
		<%--
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
		--%>
	</div>
</div>
<%-- 이동 예정. --%>
<script type="text/x-tmpl" id="tmpl-smalltalks">
{% for (var i=0; i<o.length; i++) { %}
	<li>
		<strong class="title"><pre>{%=o[i].talk%}</pre></strong>
		<div class="time">{%=o[i].time%}</div>
	</li>
{% } %}
</script>
<script src="${url:resource('/javascripts/jquery.tmpl.min.js')}"></script>
<script src="${url:resource('/javascripts/autogrow.min.js')}"></script>
<script type="text/javascript">
	var smallTalkService = {
		get : function(){
			$.get('/smalltalks', function(data) {
				$(".smallTalksUl").html( tmpl("tmpl-smalltalks", data) );
			});
		},
		save : function(){
			var that = this;
			var talk = $('#smallTalk').val();
			$.post('/smalltalks', { 'talk' : talk }, function(data) {
				if( data == 'OK' ){
					that.get();
					$('#smallTalk').val('');
				}
			});
		},
		bindAutogrow : function(){
			$('#smallTalk').autogrow();
		},
		bindSubmitBtn : function(){
			var that = this;
			$('.submitBtn').on('click', function(evt){
				evt.preventDefault();
				that.save();
			});
		}
	};
	
	$(document).ready(function(){
		smallTalkService.bindAutogrow();
		smallTalkService.bindSubmitBtn();
		
	});
</script>