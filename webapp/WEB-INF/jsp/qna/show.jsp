<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
<link href="${url:resource('/stylesheets/boards.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-style.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-textile-style.css')}" rel="stylesheet">
<link href="${url:resource('/stylesheets/wiki-imageupload-plugins.css')}" rel="stylesheet">
<style type="text/css">
.markItUpEditor { height:150px; }
</style>
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
					<div class="contents">
						<strong class="subject">${question.title}</strong>
						<div>${sf:br(question.contents)}</div>
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
					<div class="button-qna">
						<a href="/questions/${question.questionId}/form"><button class="btn">수정하기</button></a>
						<a href="/questions/${question.questionId}/delete"><button class="btn">삭제하기</button></a>
						<a href="/questions"><button class="btn">목록으로</button></a>				
					</div>
				</div>
			
				<div class="comment">
					<c:forEach items="${question.answers}" var="each">
					<div class="commentList">
						<div class="nickArea">
							<p class='prphoto'><img src='${socialUser.imageUrl}'/></p>
							<div class="nickname">
								<div class="tester"><span class='lv'>${each.writerName}</span></div>
							</div>
						</div>
						<div class="list">
							<div class="cont">${sf:br(each.contents)}</div>
							<div class="regDate"><fmt:formatDate value="${each.createdDate}" pattern="yyyy-MM-dd HH:mm" /></div>
						</div>
						<div class="commBtn">
							<a href="">수정</a>&nbsp;&nbsp;<a href="">삭제</a>
						</div>
					</div>
					</c:forEach>
				</div>
				<sec:authorize access="!hasRole('ROLE_USER')">
				<a href="/login">로그인</a>을 하면 이 글에 대한 답글을 쓸 수 있습니다.	
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_USER')">
				<div class="form">
					<form:form modelAttribute="answer" action="/questions/${question.questionId}/answers" method="POST">
						<form:textarea path="contents"  cols="75" rows="5"/>
						<div class="button">
							<button type="submit" class="btn btn-primary">답변하기</button>
						</div>					
					</form:form>
				</div>				
				</sec:authorize>
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
<script src="http://ajax.microsoft.com/ajax/jquery.validate/1.7/jquery.validate.min.js"></script>
<script type="text/javascript" src="${url:resource('/javascripts/jquery.markitup.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/jquery.autocomplete.js')}"></script>
<script type="text/javascript">
var uploaderUrl = "http://localhost:8080";
</script>
<script type="text/javascript"	src="${url:resource('/javascripts/qna/image.upload.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/qna/qna-set.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/qna/tagparser.js')}"></script>
<script type="text/javascript" src="${url:resource('/javascripts/qna/show.js')}"></script>	
</body>
</html>