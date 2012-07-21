<%@ tag language="java" pageEncoding="UTF-8"%><%@ tag body-content="empty" %><%@
attribute name="type" required="true" rtexprvalue="true" type="java.lang.Integer" description="탭 type"%><%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
      <header class="jumbotron subhead" id="subnav">
        <div class="subnav">
          <ul class="nav nav-pills">
            <li id="recently" <c:if test="${type == 1}">class="active"</c:if>><a href="/questions">최신순</a></li>
            <li id="tagManagement" <c:if test="${type == 2}">class="active"</c:if>><a href="/admin/tags">태그관리</a></li>
          </ul>
        </div>
      </header>
