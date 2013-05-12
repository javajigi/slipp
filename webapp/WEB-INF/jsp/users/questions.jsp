<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>${socialUser.displayName}의 개인공간 :: SLiPP</title>
</head>

<section class="person-content">
	<slipp:profile-header socialUser="${socialUser}"/>
	<div class="person-articles">
		<section class="qna-list">
			<h1>질문한 글목록</h1>
			<ul class="list">
			<c:forEach items="${questions.content}" end="4" var="each">
				<slipp:list each="${each}"/>
			</c:forEach>
			</ul>
			<nav class="pager">
				<ul>
					<sl:pager page="${questions}" prefixUri="${socialUser.url}/questions"/>
				</ul>
			</nav>
		</section>
	</div>
</section>
