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
				<div class="forumList">
					<c:forEach items="${questions}" var="each">
					<div class="forum">
						<div class="list">
							<a href="/qna/${each.questionId}"> <strong class="subject">${each.title}</strong>
							</a>
							<div class="count">
								<span class="tags">
									<c:forEach items="${each.tags}" var="tag">
									<a href="@{Threads.tagged(tag.name)}"><strong>${tag.name}</strong></a>	
									</c:forEach> 
								</span> 
								<span class="countAnswer">답변수 <strong>${each.answerCount}</strong></span>
							</div>
						</div>
						<div class="nickArea">${each.writerName}</div>
						<div class="regDate"><fmt:formatDate value="${each.createdDate}" pattern="yyyy-MM-dd HH:mm" /></div>
					</div>					
					</c:forEach>
				</div>

				<div class="pagination pagination-centered">
					<ul>
						<li><a href="#">&laquo;</a></li>
						<li><a href="#">10</a></li>
						<li class="active"><a href="#">11</a></li>
						<li><a href="#">12</a></li>
						<li><a href="#">&raquo;</a></li>
					</ul>
				</div>

				<div class="pull-right">
					<a href="/questions/form" class="btn btn-primary btn-large pull-right">질문하기</a>
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