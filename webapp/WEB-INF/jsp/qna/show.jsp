<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
<link href="${url:resource('/stylesheets/boards.css')}" rel="stylesheet">
</head>
<body>
	<header class="jumbotron subhead" id="subnav">
		<div class="subnav">
			<ul class="nav nav-pills">
				<li class="active"><a href="#global">최신순</a></li>
				<li><a href="#gridSystem">Hot</a></li>
				<li><a href="#fluidGridSystem">Fluid grid system</a></li>
			</ul>
		</div>
	</header>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span10">
				<div class="forumView">
					<div class="nickArea">
						<p class='nick'>${question.writerName}</p>
						<p class="regDate"><fmt:formatDate value="${question.createdDate}" pattern="yyyy-MM-dd HH:mm" /></p>
					</div>
					<div class="cont">
						<strong class="subject">${question.title}</strong>
						<div>${sf:br(question.contents)}</div>
						<div class="snsIcon">
							<div class="facebook">
								<div id="fb-root"></div>
								<script src="http://connect.facebook.net/en_US/all.js#xfbml=1"></script>
								<fb:like href="http://www.slipp.net/threads/{thread.id}"
									send="true" layout="button_count" width="100" show_faces="true"
									font=""></fb:like>
							</div>
							<div class="googleplus">
								<g:plusone></g:plusone>
							</div>
							<div class="twitter">
								<a href="http://twitter.com/share" class="twitter-share-button"
									data-count="horizontal">Tweet</a>
								<script type="text/javascript"
									src="http://platform.twitter.com/widgets.js"></script>
							</div>
							<div class="me2day">
								<a
									href="http://me2day.net/posts/new?new_post[body]=&quot;{thread.title}&quot;:http://www.slipp.net/threads/{thread.id}"
									onclick="window.open(this.href,'me2day_post', 'width=1024,height=364,scrollbars=1,resizable=1');return false;"><img
									src="/resources/images/me2day.gif" alt="미투데이로 보내기" /></a>
							</div>
						</div>
					</div>
				</div>
				<div class="follow">
					<p class="tags">
						<c:forEach items="${question.tags}" var="tag">
						<a href="@{Threads.tagged(tag.name)}"><strong>${tag.name}</strong></a>	
						</c:forEach> 
					</p>
					<p class="count">
						<span class="answerNum">답변수 <strong>${question.answerCount}</strong></span>
					</p>
				</div>
								
				<div class="button-thread">
					<button class="btn"><a href="/qna">수정하기</a></button>
					<button class="btn"><a href="/qna">삭제하기</a></button>
					<button class="btn"><a href="/qna">목록으로</a></button>				
				</div>

				<div class="comment">
					<div class="commentList">
						<div class="cont">{answer.displayContents.raw()}</div>
						<div class="cont-support">
							<div class="button-answer">
								<a href="@{Threads.updateAnswerForm(thread.id, answer.id)}">수정</a>
							</div>
							<div class="user-info">
								<div class="user-nickname">{answer.user.nickName}</div>
								<div class="user-action-time">{answer.displayDate}</div>
							</div>
						</div>
					</div>
				</div>
				<div class="form">
					<textarea id="contents" name="contents" cols="75" rows="10"></textarea>
					<br />
					<div class="button">
						<input type="submit" value="답글달기" />
					</div>
				</div>
			</div>

			<div class="span2">
				<div class="tags">
					<ul>
						<c:forEach items="${tags}" var="each">
						<li>${each.name} X ${each.taggedCount}</li>	
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
	</div>
</body>
</html>