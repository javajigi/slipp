<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>${socialUser.userId}의 개인공간 :: SLiPP</title>
</head>

<section class="person-content">
	<slipp:profile-header socialUser="${socialUser}"/>
	<div class="person-articles">
		<div class="person-articles-type-select">
			<a class="active" href="${socialUser.url}/questions">질문</a>
			<a href="${socialUser.url}/answers">답변</a>
		</div>
		<section class="qna-list">
			<ul class="list">
			<c:forEach items="${questions.content}" var="each">
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
