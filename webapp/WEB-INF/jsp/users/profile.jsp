<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>${socialUser.displayName}의 개인공간 :: SLiPP</title>
</head>

<section class="person-content">
	<slipp:profile-header socialUser="${socialUser}"/>
	<div class="person-articles">
		<section class="person-answers qna-list">
			<h1>최근글목록</h1>
			<ul class="list">
			<c:forEach items="${answers.content}" end="4" var="each">
				<slipp:list each="${each.question}"/>
			</c:forEach>
			</ul>
			<nav class="link-more">
				<a href="${socialUser.url}/answers">전체목록보기 &raquo;</a>
			</nav>
		</section>
	</div>
</section>
