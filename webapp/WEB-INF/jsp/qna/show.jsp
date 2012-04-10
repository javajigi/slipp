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
						<p class='nick'>nickName</p>
						<p class="regDate">displayDate</p>
					</div>
					<div class="cont">
						<strong class="subject">title</strong>
						<div>{thread.displayContents.raw()}</div>
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
									src="/public/images/me2day.gif" alt="미투데이로 보내기" /></a>
							</div>
						</div>
					</div>
				</div>
				<div class="button-thread">
					<a href="@{Threads.delete(thread.id)}">삭제</a> <a
						href="@{Threads.updateForm(thread.id)}">수정</a> <a
						href="@{Threads.list()}">목록</a>
				</div>
				<div class="follow">
					<p class="tags">
						<a
							href="@{Threads.tagged(tag.name)}"><strong>{tag.name}</strong></a>
					</p>
					<p class="count">
						<span class="answerNum">답변수 <strong>{thread.answerCount}</strong></span>
					</p>
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
						<li>java</li>
						<li>java1</li>
						<li>java2</li>
						<li>java3</li>
						<li>java4</li>
						<li>java5</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</body>
</html>