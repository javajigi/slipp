<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
<title>로그인 :: SLiPP</title>
<link href="${url:resource('/stylesheets/main.css')}" rel="stylesheet">
</head>
<body>
	<div class="jumbotron">
		<div class="container">
			<h2>Sustainable Life, Programming, Programmer</h2>
			<p>삶과 일의 균형을 맞추면서 행복하게 살 수 있는 세상을 꿈꿉니다.</p>
			<div class="row-fluid">
				<div class="span4">
					<input id="fbLogoutBtn" class="btn btn-primary btn-large" type="button" value="페이스북 계정으로 로그아웃" />
				</div>
			</div>			
		</div>
	</div>
	<script src="//connect.facebook.net/en_US/all.js"></script>
	<script>
	  window.fbAsyncInit = function() {
	    // init the FB JS SDK
	    FB.init({
	      appId      : '129401280541462', // App ID from the App Dashboard
	      status     : true, // check the login status upon init?
	      cookie     : true, // set sessions cookies to allow your server to access the session?
	      xfbml      : true  // parse XFBML tags on this page?
	    });
	  };
	  
	  $(document).ready(function() {
		  $("#fbLogoutBtn").click(function() {
			  FB.logout(function(response) {});  
		  });
	  });
	</script>	
</body>
</html>