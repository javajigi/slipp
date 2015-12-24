<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>회원관리 :: SLiPP</title>
</head>

<section class="taglist-content">
	<h1>회원 관리</h1>
	<form class="form-search" action="/admin/users" method="get">
		<input type="text" name="searchTerm" value="${searchTerm}" size="70"/>
		<button type="submit">검색</button>
	</form>	
	<br/>
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
				<tr <c:if test="${each.blocked}">style="background-color:orange"</c:if><c:if test="${each.admined}">style="background-color:greenyellow"</c:if> >
					<td><a href="/users/${each.id}/${each.userId}">${each.userId}</a></td>
					<td>${each.email}</td>
					<td>${each.providerId}</td>
					<td>${each.displayName}</td>
					<td>${each.createDate}</td>
					<td>
						<c:if test="${not each.blocked}">
						<form class="form-search" action="/admin/users/${each.id}/block" method="post">
							<input type="hidden" name="page" value="${users.number}" />
							<input type="hidden" name="searchTerm" value="${searchTerm}" />
							<button type="submit">계정 징계</button>
						</form>
						</c:if>
						<c:if test="${each.providerId eq 'slipp'}">
						<form class="form-search" action="/admin/users/${each.id}/resetpassword" method="post">
							<input type="hidden" name="page" value="${users.number}" />
							<button type="submit">비밀번호 재설정</button>
						</form>
						</c:if>
                        <c:choose>
                            <c:when test="${each.admined}">
                                <form class="form-search" action="/admin/users/${each.id}/unadmin" method="post">
                                    <input type="hidden" name="page" value="${users.number}" />
                                    <input type="hidden" name="searchTerm" value="${searchTerm}" />
                                    <button type="submit">관리자 지정 해제</button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form class="form-search" action="/admin/users/${each.id}/admin" method="post">
                                    <input type="hidden" name="page" value="${users.number}" />
                                    <input type="hidden" name="searchTerm" value="${searchTerm}" />
                                    <button type="submit">관리자로 지정</button>
                                </form>
                            </c:otherwise>
                        </c:choose>
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
