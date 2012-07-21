<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
<link href="${url:resource('/stylesheets/boards.css')}" rel="stylesheet">
</head>
<body>
	<slipp:header type="1"/>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span10">
				<div class="forumList">
					<c:forEach items="${questions.content}" var="each">
					<div class="forum">
						<div class="list">
							<a href="/questions/${each.questionId}"> <strong class="subject">${each.title}</strong>
							</a>
							<div class="count">
								<span class="tags">
									<c:forEach items="${each.tags}" var="tag">
									<a href="/questions/tagged/${tag.name}"><strong>${tag.name}</strong></a>	
									</c:forEach> 
								</span> 
								<span class="countAnswer">답변수 <strong>${each.answerCount}</strong></span>
							</div>
						</div>
						<div class="nickArea">${each.writer.displayName}</div>
						<div class="regDate"><fmt:formatDate value="${each.createdDate}" pattern="yyyy-MM-dd HH:mm" /></div>
					</div>					
					</c:forEach>
				</div>

				<div class="pagination pagination-centered">
					<ul>
						<sl:pager page="${questions}" prefixUri="/questions"/>
					</ul>
				</div>

				<div class="pull-right">
					<a href="/questions/form" class="btn btn-primary btn-large pull-right">질문하기</a>
				</div>
			</div>

			<slipp:tags tags="${tags}"/>
		</div>
	</div>
</body>
</html>