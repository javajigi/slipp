<%@ tag language="java" pageEncoding="UTF-8"%><%@ tag body-content="empty"
%><%@ attribute name="type" rtexprvalue="true" type="java.lang.Integer" description="탭 type"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"
%>
<c:choose>
  <c:when test="${empty currentTag}">
    <header>
      <h1>글목록</h1>
    </header>
  </c:when>
  <c:otherwise>
    <header>
    	<h1><i class="icon-tag" title="태그"></i> ${currentTag.name} <small>: ${currentTag.taggedCount}개의 글</small></h1>
    </header>
  </c:otherwise>
</c:choose>
<sec:authorize access="hasRole('ROLE_ADMINISTRATOR')">
<a href="/admin/tags">태그관리</a>
</sec:authorize>
