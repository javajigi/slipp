<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
<title>태그관리 :: SLiPP</title>
<link href="${url:resource('/stylesheets/slipp.css')}" rel="stylesheet">
</head>
<body>
	<slipp:header type="3" />

	<div class="span8">
		<slipp:tags tags="${tags}"/>
		
		<div class="pagination pagination-centered">
			<ul>
				<sl:pager page="${tags}" prefixUri="/admin/tags"/>
			</ul>
		</div>		
	</div>
	<div class="span8">
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
</body>
</html>