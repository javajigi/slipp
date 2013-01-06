<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
<title>태그관리 :: SLiPP</title>
<link href="${url:resource('/stylesheets/slipp.css')}" rel="stylesheet">
</head>
<body>
	<slipp:header type="4" />

	<div class="span8">
		<table id="tags" class="table table-bordered">
			<thead>
				<tr>
					<th class="span1">아이디</th>
					<th class="span2">이름</th>
					<th class="span2">taggedCount</th>
					<th class="span1">상태</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${newtags.content}" var="each">
				<tr <c:if test="${each.deleted}">style="background-color:orange"</c:if>>
					<td>${each.tagId}</td>
					<td>${each.name}</td>
					<td>${each.taggedCount}</td>
					<td>
						<c:choose><c:when test="${each.deleted}">완료</c:when><c:otherwise>신규</c:otherwise></c:choose>
					</td>
					<td>
						<form class="form-search" action="/admin/moveNewTag" method="post">
							<input type="hidden" name="tagId" value="${each.tagId}" />
				      		<select id="parentTag" name="parentTag">
				      			<option value="">없음</option>
				      			<c:forEach items="${parentTags}" var="each">
				      			<option value="${each.tagId}">${each.name}</option>
				      			</c:forEach>
				      		</select>							
							<button type="submit" class="btn btn-primary">태그로 추가</button>
						</form>
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<div class="pagination pagination-centered">
			<ul>
				<sl:pager page="${newtags}" prefixUri="/admin/newtags"/>
			</ul>
		</div>		
	</div>
</body>
</html>