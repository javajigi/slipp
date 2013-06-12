<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>태그관리 :: SLiPP</title>
</head>

<c:set var="tagInfo" value="${tag.tagInfo}"/>

<section class="taglist-content">
	<h1 class="article-title">태그 내용</h1>
	<div class="content-main">
		<table id="tags">
			<thead>
				<tr>
					<th>이름</th>
					<th>소유주</th>
					<th>설명</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><a href="/admin/tags/${tag.tagId}">${tag.name}</a></td>
					<c:choose>
					<c:when test="${tag.requestedTag}">
					<td>${tagInfo.owner.userId}</td>
					<td>${tagInfo.description}</td>					
					</c:when>
					<c:otherwise>
					<td></td>
					<td></td>
					</c:otherwise>
					</c:choose>
				</tr>
			</tbody>
		</table>
	</div>
</section>
