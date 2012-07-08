<%@ tag language="java" pageEncoding="UTF-8"%><%@ tag body-content="empty" %><%@
attribute name="tags" required="true" rtexprvalue="true" type="java.util.List" description="Tag 목록"%><%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="span2">
	<div class="tags">
		<ul>
			<c:forEach items="${tags}" var="each">
			<li><a href="/questions/tagged/${each.name}">${each.name}</a> X ${each.taggedCount}</li>	
			</c:forEach>
		</ul>
	</div>
</div>
