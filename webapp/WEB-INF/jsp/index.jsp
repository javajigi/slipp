<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@include file="/WEB-INF/jsp/include/tags.jspf"
%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SLiPP</title>
</head>
<body>

<div class="section-main row-fluid">
	<section class="span8">
		<div class="qna">
			<h1><a href="/questions">QnA</a></h1>
			<a id="questionBtn" href="/questions/form" class="btn btn-primary btn-large btn-question">질문하기</a>
			<div class="qna-list">
				<h2 class="hidden">list</h2>
				<ul class="list">
				<c:forEach items="${questions.content}" var="each">
					<li>
						<div class="wrap">
							<div class="main">
								<strong class="subject">
									<a href="/questions/${each.questionId}">${sf:h(each.title)}</a>
								</strong>
								<div class="tags">
									<ul>
									<c:forEach items="${each.denormalizedTags}" var="tag">
										<li>
											<a href="/questions/tagged/${tag}" class="tag">${tag}</a>	
										</li>
									</c:forEach>
									</ul>
								</div>
							</div>
							<div class="sub">
								<div class="reply">
									<i class="symbol" title="댓글">R</i>
									<span class="point">${each.answerCount}</span>
								</div>
								<div class="auth-info">
									<a href="${each.writer.profileUrl}" class="author">${each.writer.userId}</a>
									<span class="time">
										<fmt:formatDate value="${each.createdDate}" pattern="yyyy-MM-dd HH:mm" />  
									</span>
								</div>
							</div>
						</div>
					</li>
				</c:forEach>
				</ul>
				<div class="pagination pagination-centered">
					<ul>
						<sl:pager page="${questions}" prefixUri="/questions"/>
					</ul>
				</div>
			</div>
			<div class="qna-side">
				<slipp:side-tags tags="${tags}"/>
			</div>
		</div>
	</section>
	<section class="span4">
		<h1><a href="/wiki">Blog</a></h1>
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

</body>
</html>