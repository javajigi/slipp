<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>태그관리 :: SLiPP</title>
</head>

<section class="taglist-content">
	<h1>태그 목록</h1>
	<%@include file="/WEB-INF/jsp/include/admin_header.jspf"%>
	<div class="content-main">
		<slipp:tags tags="${tags}" admin="true"/>
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
