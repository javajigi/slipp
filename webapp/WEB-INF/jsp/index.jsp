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
								<i class="icon-addtext"></i>
								<span class="time">
									<fmt:formatDate value="${each.createdDate}" pattern="yyyy-MM-dd HH:mm" />
								</span>
								<a href="${each.writer.profileUrl}" class="author">${each.writer.userId}</a>
							</div>
							<div class="reply" title="댓글">
								<i class="icon-reply"></i>
								<span class="point">${each.answerCount + 1}</span>
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
		<section class="mailing">
			<h1>SLiPP Mailing</h1>
			<p>메일주소를 등록하시면 SLiPP에 올라오는 흥미로운 이야기를 모아서 정기적으로 메일을 보내드립니다.</p>
			<form action="/" class="mailing-form">
				<fieldset>
					<input type="email" class="inp-mail" placeholder="your-mail@example.com" />
					<button type="submit" class="btn-submit">등록</button>
				</fieldset>
			</form>
		</section>
		<section class="span4">
			<h1>SLiPP 소식</h1>
			<div class="forumList">
			<c:forEach items="${pages}" var="page">
				<div class="nickArea">
					<p class="regDate">${page.creationDate}</p>
				</div>
				<div class="cont">
					<strong class="subject"><a href="/wiki/pages/viewpage.action?pageId=${page.pageId}">${page.title}</a></strong>
					<div>${page.shortContents}</div>
				</div>
			</c:forEach>
			</div>
			<div class="forumTop">
				<div class="rss">
					<a href="http://feeds.feedburner.com/slipp"><img src="http://feeds.feedburner.com/~fc/slipp?bg=99CCFF&amp;fg=444444&amp;anim=0" height="26" width="88" style="border:0" alt="" /></a>
				</div>
			</div>
		</section>
	</div>
</div>
