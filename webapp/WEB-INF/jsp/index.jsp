<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@include file="/WEB-INF/jsp/include/tags.jspf"
%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SLiPP</title>
<link rel="stylesheet" media="screen" href="/public/stylesheets/boards.css">
</head>
<body>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span6">
			<h1><a href="/questions">QnA</a></h1>
			<div class="forumList">
				<c:forEach items="${questions.content}" var="each">
				<div class="forum">
					<div class="list">
						<a href="/questions/${each.questionId}"> <strong class="subject">${sf:h(each.title)}&nbsp;[${each.answerCount}]</strong>
						</a>
						<div class="count">
							<span class="tags">
								<c:forEach items="${each.denormalizedTags}" var="tag">
								<a href="/questions/tagged/${tag}"><strong>${tag}</strong></a>	
								</c:forEach> 
							</span> 
							<span class="countAnswer">
								<a href="${each.writer.profileUrl}">${each.writer.userId}</a>&nbsp;&nbsp;
								<fmt:formatDate value="${each.createdDate}" pattern="yyyy-MM-dd HH:mm" />  
							</span>
						</div>
					</div>
				</div>					
				</c:forEach>
			</div>
			
			<div class="pagination pagination-centered">
				<ul>
					<sl:pager page="${questions}" prefixUri="/questions"/>
				</ul>
			</div>
			
			<div class="pull-right">
				<a id="questionBtn" href="/questions/form" class="btn btn-primary btn-large pull-right">질문하기</a>
			</div>				
		</div>
		
		<slipp:side-tags tags="${tags}"/>
		
		<div class="span4">
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
		</div>		
	</div>
</div>
</body>
</html>