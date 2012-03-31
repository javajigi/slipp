<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"
%><%@include file="/WEB-INF/jsp/include/tags.jspf"%><!DOCTYPE html>
<html lang="ko">
<head>
<title><decorator:title default="SLiPP"/></title>
<meta charset="utf-8"/>
<link rel="stylesheet" href="${url:resource('/stylesheets/main.css')}" type="text/css" />
<decorator:head />
</head>
<body>
<!-- start header -->
<div id="header">
	<div class="top">
	<%--
	#{if isUserLoggedIn}
		${user.email}&nbsp;&nbsp;<a href="@{Application.logout()}">ë¡ê·¸ìì</a>
	#{/if}
	#{else}
		<a href="@{Application.login()}">ë¡ê·¸ì¸</a>
	#{/else}
	 --%>
	</div>
	<div id="logo">
		<h1><a href="/">SLiPP<sup></sup></a></h1>
		<h2>지속 가능한 삶, 프로그래밍, 프로그래머(sustainable life, programming, programmer)</h2>
	</div>
	<%-- #{gnb-menu /}  --%>
</div>
<!-- end header -->

<decorator:body/>

<!-- start footer -->
<div id="footer">
	<p id="legal">Design by <a href="http://www.freecsstemplates.org/">Free CSS Templates</a>.</p>
</div>
<!-- end footer -->

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/jquery-ui.min.js"></script>

<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
try{
var pageTracker = _gat._getTracker("UA-22853131-1");
pageTracker._trackPageview();
} catch(err) {}</script>
</body>
</html>