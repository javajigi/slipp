<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
<title>태그관리 :: SLiPP</title>
<link href="${url:resource('/stylesheets/boards.css')}" rel="stylesheet">
</head>
<body>
	<slipp:header type="3" />

	<div class="span8">
		<table id="tags" class="table table-bordered">
			<thead>
				<tr>
					<th class="span2">아이디</th>
					<th>이름</th>
					<th class="span2">taggedCount</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${newtags.content}" var="each">
				<tr>
					<td>${each.tagId}</td>
					<td>${each.name}</td>
					<td>${each.taggedCount}</td>
					<td>
						<form class="form-search" action="/admin/moveNewTag" method="post">
							<input type="hidden" name="tagId" value="${each.tagId}" />
							<button type="submit" class="btn btn-primary">태그로 추가</button>
						</form>
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<div class="pagination pagination-centered">
			<ul>
				<sl:pager page="${newtags}" prefixUri="/admin/tags"/>
			</ul>
		</div>		
	</div>
</body>
</html>