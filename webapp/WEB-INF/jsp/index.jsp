<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/include/tags.jspf" %>

<div class="home-content">
	<div class="content-main">
		<section class="qna-list">
			<h1>최근화제</h1>
<ul class="list">

				<li>
					<div class="wrap">
						<div class="main">
							<strong class="subject">
								<a href="/questions/170">우리는 함수형 프로그래밍을 배워야 할까?</a>
							</strong>

							<div class="auth-info">



										<i class="icon-add-comment"></i>
										<span class="type">응답</span>
										<span class="time">
											2013-07-30 10:30
										</span>
										<a href="/users/65/fupfin" class="author">fupfin</a>


							</div>
							<div class="reply" title="댓글">
								<i class="icon-reply"></i>
								<span class="point">6</span>
							</div>
						</div>
					</div>
				</li>

				<li>
					<div class="wrap">
						<div class="main">
							<strong class="subject">
								<a href="/questions/168">회사일이 재미없어 질 때 어떻게 극복하시나요?</a>
							</strong>

							<div class="auth-info">



										<i class="icon-add-comment"></i>
										<span class="type">응답</span>
										<span class="time">
											2013-07-29 08:23
										</span>
										<a href="/users/94/ezblog" class="author">ezblog</a>


							</div>
							<div class="reply" title="댓글">
								<i class="icon-reply"></i>
								<span class="point">10</span>
							</div>
						</div>
					</div>
				</li>

				<li>
					<div class="wrap">
						<div class="main">
							<strong class="subject">
								<a href="/questions/169">TestCase는 어떤식으로 작성하나요?</a>
							</strong>

							<div class="auth-info">



										<i class="icon-add-comment"></i>
										<span class="type">응답</span>
										<span class="time">
											2013-07-26 16:43
										</span>
										<a href="/users/87/benghun" class="author">benghun</a>


							</div>
							<div class="reply" title="댓글">
								<i class="icon-reply"></i>
								<span class="point">2</span>
							</div>
						</div>
					</div>
				</li>

				<li>
					<div class="wrap">
						<div class="main">
							<strong class="subject">
								<a href="/questions/163">람다식이 가져다줄 자바프로그래밍의 변화는 무엇이 있을까요?</a>
							</strong>

							<div class="auth-info">



										<i class="icon-add-comment"></i>
										<span class="type">응답</span>
										<span class="time">
											2013-07-25 23:35
										</span>
										<a href="/users/85/ologist" class="author">ologist</a>


							</div>
							<div class="reply" title="댓글">
								<i class="icon-reply"></i>
								<span class="point">9</span>
							</div>
						</div>
					</div>
				</li>

				<li>
					<div class="wrap">
						<div class="main">
							<strong class="subject">
								<a href="/questions/167">WAS에서 멀티 인스턴스를 사용할 수 있잖아요, 그럼 그 인스턴스란 어떤 개념인가요?</a>
							</strong>

							<div class="auth-info">



										<i class="icon-add-comment"></i>
										<span class="type">응답</span>
										<span class="time">
											2013-07-23 17:16
										</span>
										<a href="/users/187/minkyong.an" class="author">minkyong.an</a>


							</div>
							<div class="reply" title="댓글">
								<i class="icon-reply"></i>
								<span class="point">5</span>
							</div>
						</div>
					</div>
				</li>

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
						<textarea id="smallTalkMessage" name="smallTalkMessage" class="tf-smalltalk-form-msg"></textarea>
						<div class="smalltalk-form-util">
							<p class="smalltalk-form-util-msg"><i class="icon-smalltalk-msg"></i> 요즘 어떠세요?</p>
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
					<button class="btn-smalltalk-list-expand">more <i class="icon-smalltalk-expand"></i></button>
				</li>
			</ul>
			<p class="smalltalk-notice">* 최근 10개까지만 보여집니다.</p>
		</section>
		<section class="notice">
			<h1><a href="/wiki/display/slipp/Home">SLiPP log</a></h1>
			<ul class="notice-list">
				<c:forEach items="${pages}" var="page">
				<li class="notice-list-item">
					<strong class="notice-item-title"><a href="/wiki/pages/viewpage.action?pageId=${page.pageId}">${page.title}</a></strong>
					<div class="notice-list-item-time">${page.creationDate}</div>
					<div class="notice-list-item-cont">
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
<%-- <script src="${url:resource('/javascripts/autogrow.min.js')}"></script> --%>
<script type="text/x-tmpl" id="tmpl-smalltalk-list">
{% for (var i=0; i<o.length; i++) { %}
	<li class="smalltalk-list-item smalltalk-list-item-${status.count}">
		<div class="smalltalk-list-item-info">
			<span class="smalltalk-list-item-info-time">{%=o[i].time%}</span>
		</div>
		<div class="smalltalk-list-item-cont">{%=o[i].talk%}</div>
	</li>
{% } %}
</script>
<script type="text/javascript">
	var smalltalkService = {
		init: function() {
			var that = this;

			// $(that.MessageField).autogrow();
			$('.smalltalk-form').on('submit', function(evt){
				evt.preventDefault();
				that.save();
			});
			$('.btn-smalltalk-list-expand').on('click', function(evt) {
				that.expand();
			})
		},
		MessageField: '#smallTalkMessage',
		save : function() {
			var that = this;
			var $talk = $(that.MessageField);

			$.post('/smalltalks', { 'talk' : $talk.val() }, function(data) {
				if (data == 'OK') {
					that.get();
					$talk.val('');
				}
			});
			that.expand();
		},
		get : function() {
			$.get('/smalltalks', function(data) {
				$('.smalltalk-list').html( tmpl('tmpl-smalltalk-list', data) );
			});
		},
		expand: function() {
			$('.smalltalk').removeClass('ui-smalltalk-list-collapse');
		}
	};

	$(document).ready(function(){
		smalltalkService.init();
	});
</script>
