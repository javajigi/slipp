<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SLiPP</title>
<link rel="stylesheet" media="screen" href="/public/stylesheets/boards.css">
</head>
<body>
<!-- start page -->
<div id="page">
	<!-- start content -->
	<div id="content">
		<div class="forumTop">
			<div class="rss">
				<a href="http://feeds.feedburner.com/slipp"><img src="http://feeds.feedburner.com/~fc/slipp?bg=99CCFF&amp;fg=444444&amp;anim=0" height="26" width="88" style="border:0" alt="" /></a>
			</div>
		</div>
		<c:forEach items="${pages}" var="page">
		<div class="forumView">
			<div class="nickArea"> 
				<p class='nick'>자바지기</p>
				<p class="regDate">${page.creationDate}</p> 
			</div>
			<div class="cont">
				<strong class="subject"><a href="/wiki/pages/viewpage.action?pageId=${page.pageId}">${page.title}</a></strong>
				<div>${page.shortContents}</div>
			</div>
		</div>
		</c:forEach>
	</div>
	<!-- end content -->
	<!-- start sidebar -->
	<div id="sidebar">
		<ul>
			<li><a href="http://www.javajigi.net">자바지기</a></li>
			<li><a href="http://www.facebook.com/javajigi">자바지기 페이스북</a></li>
			<li><a href="https://twitter.com/#!/javajigi">자바지기 트위터</a></li>
		</ul>
	</div>
	<!-- end sidebar -->
	<div style="clear: both;">&nbsp;</div>
</div>
<!-- end page -->
</body>
</html>