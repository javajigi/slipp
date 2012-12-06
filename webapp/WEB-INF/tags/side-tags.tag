<%@ tag language="java" pageEncoding="UTF-8"%><%@ tag body-content="empty" %><%@
attribute name="tags" required="true" rtexprvalue="true" type="java.util.List" description="Tag 목록"%><%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="tags">
	<h2 class="">tags</h2>
	<ul>
		<c:forEach items="${tags}" var="each">
		<li>
			<a href="/questions/tagged/${each.name}" class="tag">
				${each.name}
				<span class="count">${each.taggedCount}</span>
			</a>
		</li>	
		</c:forEach>
	</ul>
</div>
