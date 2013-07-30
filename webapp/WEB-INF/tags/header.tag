<%@ tag language="java" pageEncoding="UTF-8"%><%@ tag body-content="empty"
%><%@ attribute name="type" rtexprvalue="true" type="java.lang.Integer" description="탭 type"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"
%><%@ taglib prefix="sf" uri="http://slipp.net/functions"%>
<c:choose>
  <c:when test="${empty currentTag}">
    <header>
      <h1>글목록</h1>
    </header>
  </c:when>
  <c:otherwise>
    <header>
    	<h1><i class="icon-tag" title="태그"></i> ${currentTag.name} <small>: ${currentTag.taggedCount}개의 글</small></h1>
    	<c:set var="tagInfo" value="${currentTag.tagInfo}"/>
    	<c:if test="${sf:isWriter(tagInfo.owner, loginUser)}">
    	<a href="/tags/${currentTag.tagId}/form">수정</a>
    	</c:if>
    	<c:if test="${currentTag.connectGroup}">
		<a href="${tagInfo.groupUrl}" target="_blank"><i class="foundicon-facebook"></i></a>
		</c:if>
    </header>
  </c:otherwise>
</c:choose>
