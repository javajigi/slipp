<!DOCTYPE html>
<html lang="ko">
    <head>
        <title>#{get 'title' /}</title>
        <meta charset="utf-8">
        <link rel="stylesheet" media="screen" href="@{'/public/stylesheets/main.css'}">
        #{get 'moreStyles' /}
        <link rel="shortcut icon" type="image/png" href="@{'/public/images/favicon.png'}">
    </head>
<body>
<!-- start header -->
<div id="header">
	<div class="top">
	#{if isUserLoggedIn}
		${user.email}&nbsp;&nbsp;<a href="@{Application.logout()}">로그아웃</a>
	#{/if}
	#{else}
		<a href="@{Application.login()}">로그인</a>
	#{/else}
	</div>
	<div id="logo">
		<h1><a href="/">SLiPP<sup></sup></a></h1>
		<h2>지속가능한 삶, 프로그래밍, 프로그래머(sustainable life, programming, programmer)</h2>
	</div>
	#{gnb-menu /}
</div>
<!-- end header -->

#{doLayout /}

<!-- start footer -->
<div id="footer">
	<p id="legal">Design by <a href="http://www.freecsstemplates.org/">Free CSS Templates</a>.</p>
</div>
<!-- end footer -->

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/jquery-ui.min.js"></script>
<script type="text/javascript">
document.domain = "slipp.net";
</script>

#{get 'moreScripts' /}

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

