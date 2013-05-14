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
<script src="${url:resource('/javascripts/slipp.js')}"></script>
<script src="${url:resource('/javascripts/localization/message.kr.js')}"></script>
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
					<sec:authorize access="hasRole('ROLE_ADMINISTRATOR')">
					<li>
						<a href="/admin/tags" id="tagManagement"><i class="icon-list"></i> <span class="text">태그관리</span></a>
					</li>
					</sec:authorize>
					<li class="site-search">
						<a href="#siteSearchArea" id="siteSearchButton" class="site-search-button"><i class="icon-search"></i></a>
					</li>
				</ul>
			</nav>
			<nav class="user-menu">
				<ul role="menu">
					<sec:authorize access="!hasRole('ROLE_USER')">
					<li class="msg-for-login">SLiPP 계정으로 의견을 나누세요! &rarr;</li>
					<li class="loginout">
						<a href="/users/login" class="link-loginout" title="SLiPP 계정연결"><i class="icon-loginout"></i></a>
					</li>
					</sec:authorize>
					<sec:authorize access="hasRole('ROLE_USER')">
					<li>
						<a id="writeBtn" href="/questions/form" class="link-write"><i class="icon-write"></i> <span class="text">새글쓰기</span></a>
					</li>
					<li class="user-info">
						<a href="/notifications" id="notificationButton" class="notification-button">
							<img class="user-thumb" src="${sf:stripHttp(loginUser.imageUrl)}" width="24" height="24" alt="" />
							<span class="user-name">${loginUser.displayName}</span>
							<c:if test="${countNotifications != 0}">
								<span class="notification-count">${countNotifications}</span>
							</c:if>
						</a>
						<div id="notificationLayer" class="notification-layer">
							<a href="${loginUser.url}" class="link-to-personalize"><i class="icon-person"></i> 나의 개인공간 &rarr;</a>
							<strong class="title">응답알림</strong>
							<ul></ul>
						</div>
					</li>
					<li class="loginout">
						<a href="/users/logout" class="link-loginout" title="로그아웃"><i class="icon-loginout"></i></a>
					</li>
					</sec:authorize>
				</ul>
			</nav>
		</div>
	</header>
	<div class="content" role="main">
		<div class="container">
			<div id="siteSearchArea" class="site-search-area">
				<gcse:search></gcse:search>
				<gcse:searchresults></gcse:searchresults>
			</div>
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
						<a href="https://github.com/javajigi/slipp/issues" target="_blank">Ideas&amp;Bugs</a>
					</li>
					<li>
						<a href="/wiki">SLiPP-Wiki</a>
					</li>
				</ul>
			</nav>
			<p class="footer-text">SLiPP - Sustainable Life, Programming, Programmer</p>
		</div>
	</footer>
</div>
<script>
(function() {
	var cx = '010235842937876666941:4opvrjfw190';
	var gcse = document.createElement('script');
	gcse.type = 'text/javascript';
	gcse.async = true;
	gcse.src = (document.location.protocol == 'https:' ? 'https:' : 'http:') + '//www.google.com/cse/cse.js?cx=' + cx;
	var s = document.getElementsByTagName('script')[0];
	s.parentNode.insertBefore(gcse, s);
})();
</script>
<script type="text/javascript">
  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-22853131-1']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();
</script>
<script>
try{
	var pageTracker = _gat._getTracker("UA-22853131-1");
	pageTracker._trackPageview();
} catch(err) {}

$(document).ready(function(){
	var $notificationLayer = $('#notificationLayer');
	var $notificationButton = $('#notificationButton');
	var $siteSearchButton = $('#siteSearchButton');

	$('body').on('click', function() {
		$notificationLayer.hide();
	});

	$notificationButton.on('click', function(e){
		e.stopPropagation();
		e.preventDefault();
		getNotificationData();
		$notificationLayer.toggle();
	});

	$siteSearchButton.on('click', function(e) {
		var $siteSearchArea = $('#siteSearchArea');
		e.preventDefault();
		$siteSearchArea.toggleClass('active');
		if ($siteSearchArea.hasClass('active')) {
			$siteSearchArea.find('input[type="text"]').focus();
		}
	})

	function getNotificationData() {
		var $btn = $notificationButton;
		var $layer = $notificationLayer;

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
});

</script>
</body>
</html>
