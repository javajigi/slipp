<%@ tag language="java" pageEncoding="UTF-8"%><%@ tag body-content="empty" %><%@
attribute name="tags" required="true" rtexprvalue="true" type="org.springframework.data.domain.Page" description="Tag 목록"%><%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<table id="tags" class="table table-bordered">
			<thead>
				<tr>
					<th class="span4">이름</th>
					<th class="span2">태그된 질문 수</th>
					<th class="span4">부모 태그</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${tags.content}" var="each">
				<tr>
					<td>${each.name}</td>
					<td>${each.taggedCount}</td>
					<td>
						<c:if test="${not empty each.parent}">
						${each.parent.name}
						</c:if>
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
