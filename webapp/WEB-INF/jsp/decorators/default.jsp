<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"
%><%@include file="/WEB-INF/jsp/include/tags.jspf"%><!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><decorator:title default="SLiPP"/></title>
<link rel="shortcut icon" type="image/x-icon" href="${url:resource('/images/favicon.ico')}">
<link href='http://fonts.googleapis.com/css?family=Oswald' rel='stylesheet' type='text/css'>
<link href="${url:resource('/stylesheets/slipp.css')}" rel="stylesheet">
<!--[if lt IE 9]>
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<decorator:head />
</head>

<body>
<div class="wrapper">
	<header class="header" role="banner">
		<div class="container">
			<h1 class="logo">
				<a href="/">SLiPP</a>
			</h1>
			<nav class="site-nav">
				<ul>
					<li>
						<a href="/questions"><i class="icon-list"></i> <span class="text">글목록</span></a>
					</li>
				</ul>
			</nav>
			<form class="site-search" action="/search">
				<fieldset>
					<i class="icon-search"></i>
					<input type="search" class="inp-search" name="q" placeholder="search" />
				</fieldset>
			</form>
			<nav class="user-menu">
				<ul role="menu">
					<sec:authorize access="!hasRole('ROLE_USER')">
					<li class="msg-for-login">로그인해서 의견을 나누세요!</li>
					<li class="loginout">
						<a href="/login" class="link-loginout"><span class="text">LogIn</span> <i class="icon-loginout"></i></a>
					</li>
					</sec:authorize>
					<sec:authorize access="hasRole('ROLE_USER')">
					<li>
						<a href="/questions/form"><i class="icon-write"></i> <span class="text">새글쓰기</span></a>
					</li>
					<li class="user-info">
						<a href="/notifications" class="notification-button">
							<img class="user-thumb" src="//graph.facebook.com/1701115026/picture" width="24" height="24" alt="" />
							<span class="user-name">진우</span>
							<c:if test="${countNotifications != 0}">
								<span class="notification-count">${countNotifications}</span>
							</c:if>
						</a>
						<div class="notification-layer">
							<strong class="title">응답알림</strong>
							<ul></ul>
						</div>
					</li>
					<li class="loginout">
						<a href="/logout" class="link-loginout"><span class="text">LogOut</span> <i class="icon-loginout"></i></a>
					</li>
					</sec:authorize>
				</ul>
			</nav>
		</div>
	</header>
	<div class="content" role="main">
		<div class="container">
			<decorator:body/>
		</div>
	</div>
	<footer class="footer">
		<div class="container">
			<nav class="foot-nav">
				<ul role="menu">
					<li>
						<a href="/about">About</a>
					</li>
					<li>
						<a href="/code">Code</a>
					</li>
					<li>
						<a href="/wiki">Wiki</a>
					</li>
					<li>
						<a href="https://github.com/javajigi/slipp/issues" target="_blank">Ideas&amp;Bugs</a>
					</li>
				</ul>
			</nav>
			<p class="footer-text">SLiPP - Sustainable Life, Programming, Programmer</p>
		</div>
	</footer>
</div>

<script>
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));

try{
	var pageTracker = _gat._getTracker("UA-22853131-1");
	pageTracker._trackPageview();
} catch(err) {}

$(document).ready(function(){
	getNotificationData();
	$('body').on('click', function() {
		closeNotificationLayer();
	});
	$('.notification-button').on('click', function(e){
		e.stopPropagation();
		e.preventDefault();
		toggleNotificationLayer();
	});
});

function toggleNotificationLayer() {
	$('.notification-layer').toggle();
}
function closeNotificationLayer() {
	$('.notification-layer').hide();
}
function getNotificationData() {
	var $btn = $('.notification-button');
	var $layer = $('.notification-layer');

	$.getJSON($btn.attr('href'), function(result){
		var items = result;
		var length = items.length;
		var $ul = $('<ul></ul>');
		var item;

		for(var i=0; i < length; i++) {
			item = items[i];

			$("<li></li>")
			.append(
				$('<a></a>')
				.attr('href', '/questions/' + item.questionId)
				.text('"' + item.title + '" 글에 댓글이 달렸습니다.')
			)
			.appendTo($ul);
		}
		$btn.find('.notification-count').text('0');

		$layer.find($('ul')).replaceWith($ul);
	});
}
</script>
</body>
</html>
