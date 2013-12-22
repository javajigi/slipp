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
	<li class="smalltalk-item smalltalk-item-${status.count}" data-smalltalk-id="${smallTalk.smallTalkId}">
		<div class="smalltalk-item-info">
			<strong class="smalltalk-item-info-author">${smallTalk.writer.userId}</strong>
			<span class="smalltalk-item-info-time">${smallTalk.time}</span>
		</div>
		<div class="smalltalk-item-cont">
			${sf:plainText(sf:removeLink(smallTalk.talk))}
			<sec:authorize access="hasRole('ROLE_USER')">
				<a class="btn-smalltalk-reply" href="javacript:;" role="button" data-smalltalk-id="${smallTalk.smallTalkId}">
					<i class="icon-smalltalk-reply"></i> 답글
				</a>
			</sec:authorize>
		</div>
		<c:if test="${not empty smallTalk.siteSummary.targetUrl}">
			<div class="smalltalk-item-summary">
				<c:if test="${not empty smallTalk.siteSummary.thumbnailImage}">
					<img class="smalltalk-item-summary-thumb" src="${smallTalk.siteSummary.thumbnailImage}" width="50" height="50"/>
				</c:if>
				<div class="smalltalk-item-summary-text">
					<strong class="smalltalk-item-summary-title">${smallTalk.siteSummary.title}</strong>
					<div class="smalltalk-item-summary-cont">${sf:cut(smallTalk.siteSummary.contents, 30, "...")}</div>
				</div>
				<a class="smalltalk-item-summary-link" href="${smallTalk.siteSummary.targetUrl}" target="_blank" title="링크로 이동">더보기</a>
			</div>
		</c:if>
		<div class="smalltalk-item-replylist">
			댓글 : <a href="#" class="smalltalk-item-show-comments">${fn:length(smallTalk.smallTalkComments) }</a> 
		</div>
		<div class="smalltalk-item-replyform"></div>
	</li>
</c:forEach>
<li class="smalltalk-list-expand">
	<button class="btn-smalltalk-list-expand" data-smalltalk-count="${fn:length(smallTalks)}">more <i class="icon-smalltalk-expand"></i></button>
</li>