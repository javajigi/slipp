<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
<title>태그관리 :: SLiPP</title>
<link href="${url:resource('/stylesheets/boards.css')}" rel="stylesheet">
</head>
<body>
	<slipp:header type="2" />

	<div class="span8">
		<table id="tags" class="table table-bordered">
			<thead>
				<tr>
					<th class="span2">아이디</th>
					<th>이름</th>
					<th class="span2">taggedCount</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${tags.content}" var="each">
				<tr>
					<td>${each.tagId}</td>
					<td>${each.name}</td>
					<td>${each.taggedCount}</td>
					<!-- 
					<td><button class="btn btn-primary" href="#">수정</button>&nbsp;&nbsp;
						<button class="btn btn-danger" href="#">삭제</button></td>
					 -->
				</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<div class="pagination pagination-centered">
			<ul>
				<sl:pager page="${tags}" prefixUri="/admin/tags"/>
			</ul>
		</div>		
	</div>
	<div class="span8">
		<form id="tagForm" class="form-search" action="/admin/tags" method="post">
      		<input type="text" id="name" name="name" class="input-medium">
      		<button type="submit" class="btn btn-primary">추가</button>
      		<c:if test="${not empty errorMessage}">
      		<label for="tag" class="error" style="">${errorMessage}</label>
      		</c:if>
    	</form>
	</div>
</body>
</html>