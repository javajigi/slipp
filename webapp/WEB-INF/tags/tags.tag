<%@ tag language="java" pageEncoding="UTF-8"%><%@ tag body-content="empty" %><%@
attribute name="tags" required="true" rtexprvalue="true" type="org.springframework.data.domain.Page" description="Tag 목록"%><%@
attribute name="admin" required="true" rtexprvalue="false" type="java.lang.Boolean" description="관리자 유무"%><%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<table id="tags">
	<thead>
		<tr>
			<th>이름</th>
			<th>태그된 질문 수</th>
			<th>부모 태그</th>
			<c:if test="${admin}">
			<th></th>
			</c:if>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${tags.content}" var="each">
		<tr <c:if test="${not each.pooled}">style="background-color:orange" class="newTags"</c:if>>
			<td>${each.name}</td>
			<td>${each.taggedCount}</td>
			<td>
				<c:if test="${not empty each.parent}">
				${each.parent.name}
				</c:if>
			</td>
			<c:if test="${admin}">
			<td>
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
			</td>
			</c:if>
		</tr>
		</c:forEach>
	</tbody>
</table>
