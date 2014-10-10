<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>회원관리 :: SLiPP</title>
</head>

<section class="taglist-content">
	<h1>회원 목록</h1>
	<div class="content-main">
		<table id="users">
			<thead>
				<tr>
					<th>사용자 아이디</th>
					<th>이메일</th>
					<th>provider</th>
					<th>Display Name</th>
					<th>회원가입일</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${users.content}" var="each">
				<tr>
					<td>${each.userId}</td>
					<td>${each.email}</td>
					<td>${each.providerId}</td>
					<td>${each.displayName}</td>
					<td>${each.createDate}</td>
					<td>
						<c:if test="${not empty each.email}">
						<form class="form-search" action="/admin/users/${each.id}/resetpassword?page=${users.number}" method="post">
							<button type="submit">비밀번호 재설정</button>
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
				<sl:pager page="${users}" prefixUri="/admin/users"/>
			</ul>
		</nav>
	</div>
</section>
