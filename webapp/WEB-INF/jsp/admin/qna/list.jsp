<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>질문 관리 :: SLiPP</title>
</head>

<section class="taglist-content">
	<h1>질문 관리</h1>
	<div>
		<form class="form-search" action="/admin/questions" method="get">
			<input type="text" name="searchTerm" size="70"/>
			<button type="submit">검색</button>
		</form>
		<br/>
		<table id="users">
			<thead>
				<tr>
					<th>제목</th>
					<th>생성일시</th>
					<th>작성자</th>
					<th>댓글수</th>
					<th>조회수</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${questions.content}" var="each">
				<tr <c:if test="${each.deleted}">style="background-color:orange"</c:if>>
					<td width="400"><a href="/admin/questions/${each.questionId}?searchTerm=${searchTerm}">${each.title}</a></td>
					<td><fmt:formatDate value="${each.createdDate}" pattern="yyyy-MM-dd HH:mm" /></td>
					<td><a href="${each.writer.url}" class="author">${each.writer.userId}</a></td>
					<td>${each.answerCount}</td>
					<td>${each.showCount}</td>
					<td>
						<a href="/admin/questions/${each.questionId}/form?searchTerm=${searchTerm}">수정</a>
						<c:if test="${not each.deleted}">
						<form class="form-search" action="/admin/questions/${each.questionId}" method="post">
							<input type="hidden" name="_method" value="DELETE"/>
							<input type="hidden" name="searchTerm" value="${searchTerm}" />
							<button type="submit">삭제</button>
						</form>
						</c:if>
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>		
	</div>	
	<div class="content-main">
		<nav class="pager">
			<ul>
				<sl:pager page="${questions}" prefixUri="/admin/questions"/>
			</ul>
		</nav>
	</div>
</section>
