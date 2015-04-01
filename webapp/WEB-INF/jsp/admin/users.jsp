<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>회원관리 :: SLiPP</title>
</head>

<section class="taglist-content">
	<h1>회원 목록</h1>
	<%@include file="/WEB-INF/jsp/include/admin_header.jspf"%>
	<div>
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
				<tr <c:if test="${each.blocked}">style="background-color:orange"</c:if>>
					<td><a href="/users/${each.id}/${each.userId}">${each.userId}</a></td>
					<td>${each.email}</td>
					<td>${each.providerId}</td>
					<td>${each.displayName}</td>
					<td>${each.createDate}</td>
					<td>
						<c:if test="${not each.blocked}">
						<form class="form-search" action="/admin/users/${each.id}/block?page=${users.number}" method="post">
							<button type="submit">계정 징계</button>
						</form>
						</c:if>
						<c:if test="${each.providerId eq 'slipp'}">
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
