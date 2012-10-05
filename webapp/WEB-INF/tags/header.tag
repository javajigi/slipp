<%@ tag language="java" pageEncoding="UTF-8"%><%@ tag body-content="empty" 
%><%@ attribute name="type" required="true" rtexprvalue="true" type="java.lang.Integer" description="탭 type"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"
%>
      <header class="jumbotron subhead" id="subnav">
        <div class="subnav">
          <ul class="nav nav-pills">
            <li id="recently" <c:if test="${type == 1}">class="active"</c:if>><a href="/questions">QnA</a></li>
            <li id="recently" <c:if test="${type == 2}">class="active"</c:if>><a href="/tags">Tags</a></li>
            <sec:authorize access="hasRole('ROLE_ADMINISTRATOR')">
            <li id="tagManagement" <c:if test="${type == 3}">class="active"</c:if>><a href="/admin/tags">태그관리</a></li>
            <li id="newTagManagement" <c:if test="${type == 4}">class="active"</c:if>><a href="/admin/newtags">신규태그관리</a></li>
            </sec:authorize>
          </ul>
        </div>
      </header>
