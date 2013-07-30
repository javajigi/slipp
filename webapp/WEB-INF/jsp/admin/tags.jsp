<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>태그관리 :: SLiPP</title>
</head>

<section class="taglist-content">
	<h1 class="article-title">태그 목록</h1>
	<div class="content-main">
		<table id="tags">
			<thead>
				<tr>
					<th>이름</th>
					<th>태그된 질문 수</th>
					<th>부모 태그</th>
					<th>사용자 요청 태그</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${tags.content}" var="each">
				<tr <c:if test="${not each.pooled}">style="background-color:orange" class="newTags"</c:if>>
					<td><a href="/admin/tags/${each.tagId}">${each.name}</a></td>
					<td>${each.taggedCount}</td>
					<td>
						<c:if test="${not empty each.parent}">
						${each.parent.name}
						</c:if>
					</td>
					<td>${each.requestedTag}</td>
					<td>
						<c:if test="${not each.pooled}">
						<form class="form-search" action="/admin/moveNewTag" method="post">
							<input type="hidden" name="tagId" value="${each.tagId}" />
				      		<select id="parentTag" name="parentTag" style="width:50%;">
				      			<option value="">없음</option>
				      			<c:forEach items="${parentTags}" var="pooledTag">
				      			<option value="${pooledTag.tagId}">${pooledTag.name}</option>
				      			</c:forEach>
				      		</select>							
							<button id="moveToPoolTagBtn" type="submit">태그로 추가</button>
						</form>
						</c:if>			
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<nav class="pager">
			<ul>
				<sl:pager page="${tags}" prefixUri="/admin/tags"/>
			</ul>
		</nav>
	</div>
	<div class="content-sub">
		<div>
			<form id="tagForm" class="form-search" action="/admin/tags" method="post">
				<input type="text" id="name" name="name" class="input-medium">
				<select id="parentTag" name="parentTag">
					<option value="">없음</option>
					<c:forEach items="${parentTags}" var="each">
					<option value="${each.tagId}">${each.name}</option>
					</c:forEach>
				</select>
				<button type="submit" class="btn btn-primary">추가</button>
				<c:if test="${not empty errorMessage}">
				<label for="tag" class="error" style="">${errorMessage}</label>
				</c:if>
			</form>
		</div>
	</div>
</section>
