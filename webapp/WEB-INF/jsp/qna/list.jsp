<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
</head>
<body>

<div class="section-qna">
	<h1 class="hidden">QnA</h1>
	<slipp:header type="1"/>
	<div class="row-fluid">
		<div class="span9 qna-list">
		  <a id="questionBtn" href="/questions/form" class="btn btn-primary btn-large btn-question">질문하기</a>
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
		<div class="span3 qna-side">
			<a id="questionBtn" href="/questions/form" class="btn btn-primary btn-large btn-block btn-question">질문하기</a>
			<slipp:side-tags tags="${tags}"/>
		</div>
	</div>
</div>

</body>
</html>