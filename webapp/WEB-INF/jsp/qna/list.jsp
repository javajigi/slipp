<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
<link href="${url:resource('/stylesheets/boards.css')}" rel="stylesheet">
</head>
<body>
      <header class="jumbotron subhead" id="subnav">
        <div class="subnav">
          <ul class="nav nav-pills">
            <li class="active"><a href="#global">최신순</a></li>
            <li><a href="#gridSystem">Hot</a></li>
            <li><a href="#fluidGridSystem">Fluid grid system</a></li>
          </ul>
        </div>
      </header>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span10">
				<div class="forumList">
					<div class="forum">
						<div class="list">
							<a href="/qna/1"> <strong class="subject">Title</strong>
							</a>
							<div class="count">
								<span class="tags"> <a href="@{Threads.tagged(tag.name)}"><strong>java</strong></a>
								</span> <span class="countAnswer">답변수 <strong>123</strong></span>
							</div>
						</div>
						<div class="nickArea">nickname</div>
						<div class="regDate">2012-03-23 11:21</div>
					</div>
					<div class="forum">
						<div class="list">
							<a href="@{Threads.show(thread.id)}"> <strong class="subject">이것은
									질문입니다. 재미있는 질문</strong>
							</a>
							<div class="count">
								<span class="tags"> <a href="@{Threads.tagged(tag.name)}"><strong>java2</strong></a>
								</span> <span class="countAnswer">답변수 <strong>123</strong></span>
							</div>
						</div>
						<div class="nickArea">nickname</div>
						<div class="regDate">2012-03-23 11:21</div>
					</div>
					<div class="forum">
						<div class="list">
							<a href="@{Threads.show(thread.id)}"> <strong class="subject">이것은
									질문입니다. 재미있는 질문</strong>
							</a>
							<div class="count">
								<span class="tags"> <a href="@{Threads.tagged(tag.name)}"><strong>java2</strong></a>
								</span> <span class="countAnswer">답변수 <strong>123</strong></span>
							</div>
						</div>
						<div class="nickArea">nickname</div>
						<div class="regDate">2012-03-23 11:21</div>
					</div>
					<div class="forum">
						<div class="list">
							<a href="@{Threads.show(thread.id)}"> <strong class="subject">이것은
									질문입니다. 재미있는 질문</strong>
							</a>
							<div class="count">
								<span class="tags"> <a href="@{Threads.tagged(tag.name)}"><strong>java2</strong></a>
								</span> <span class="countAnswer">답변수 <strong>123</strong></span>
							</div>
						</div>
						<div class="nickArea">nickname</div>
						<div class="regDate">2012-03-23 11:21</div>
					</div>
				</div>

				<div class="pagination pagination-centered">
					<ul>
						<li><a href="#">&laquo;</a></li>
						<li><a href="#">10</a></li>
						<li class="active"><a href="#">11</a></li>
						<li><a href="#">12</a></li>
						<li><a href="#">&raquo;</a></li>
					</ul>
				</div>

				<div class="pull-right">
					<a href="/qna/form" class="btn btn-primary btn-large pull-right">질문하기</a>
				</div>
			</div>

			<div class="span2">
				<div class="tags">
					<ul>
						<li>java</li>
						<li>java1</li>
						<li>java2</li>
						<li>java3</li>
						<li>java4</li>
						<li>java5</li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<p>
		Hello World :

		<c:choose>
			<c:when test="${securityLevel eq 'Protected'}">
Protected Area.
</c:when>
			<c:when test="${securityLevel eq 'Public'}">

Public Area. 
<p>
					<a href="/protected">Attempt to access</a> a protected resource
				</p>
			</c:when>
		</c:choose>
	</p>

	<sec:authorize access="!hasRole('ROLE_USER')">
		<p>
			You are not logged in. &nbsp;<a href="/oauthlogin.jsp" />Login</a>
		</p>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_USER')">
You are logged in locally as <c:out value="${userName}" />. &nbsp;<a
			href="/logout">Logout</a>
		</p>

	</sec:authorize>

	<sec:authorize access="hasRole('ROLE_USER_TWITTER')">
		<p>You are connected with Twitter.</p>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_USER_FACEBOOK')">
		<p>You are connected with Facebook.</p>
	</sec:authorize>


	<sec:authorize
		access="hasRole('ROLE_USER') and !hasRole('ROLE_USER_FACEBOOK')">
		<p>
			<a href="/oauthconnect.jsp">Connect</a> your account with Facebook
		</p>
	</sec:authorize>
	<sec:authorize
		access="hasRole('ROLE_USER') and !hasRole('ROLE_USER_TWITTER')">
		<p>
			<a href="/oauthconnect.jsp">Connect</a> your account with Twitter
		</p>
	</sec:authorize>

</body>
</html>