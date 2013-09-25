<%@ page pageEncoding="UTF-8" %><%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@
taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@
taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%><%@
taglib prefix="spring" uri="http://www.springframework.org/tags"%><%@
taglib prefix="form" uri="http://www.springframework.org/tags/form"%><%@
taglib prefix="sec" uri="http://www.springframework.org/security/tags"%><%@
taglib prefix="url" uri="http://www.slipp.net/url"%><%@
taglib prefix="sf" uri="http://slipp.net/functions"%><%@
taglib prefix="sl" uri="http://www.slipp.net/tags"%><%@
taglib prefix="slipp" tagdir="/WEB-INF/tags" %>
	<c:forEach items="${smallTalks}" var="smallTalk" varStatus="status">
	<li class="smalltalk-list-item smalltalk-list-item-${status.count}">
		<div class="smalltalk-list-item-info">
			<strong class="smalltalk-list-item-info-author">${smallTalk.writer.userId}</strong>
			<span class="smalltalk-list-item-info-time">${smallTalk.time}</span>
		</div>
		<div class="smalltalk-list-item-cont">${sf:removeLink(smallTalk.talk)}</div>
		<c:if test="${not empty smallTalk.siteSummary.targetUrl}">
		<div class="" style="width: 100%">
		<table width="100%" border="1">
			<tr>
				<td colspan="2">${sf:linksToTitle(smallTalk.siteSummary.targetUrl, smallTalk.siteSummary.title)}</td>
			<tr>
			<tr>
				<td><img src="${smallTalk.siteSummary.thumbnailImage}" width="50px" height="50px"/></td>
				<td>${sf:cut(smallTalk.siteSummary.contents, 30, "...")}</td>
			</tr>
		</table>
		</div>
		</c:if>
	</li>
	</c:forEach>
	<li class="smalltalk-list-expand">
		<button class="btn-smalltalk-list-expand" data-smalltalk-count="${fn:length(smallTalks)}">more <i class="icon-smalltalk-expand"></i></button>
	</li>
