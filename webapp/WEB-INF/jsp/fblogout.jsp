<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>로그인 :: SLiPP</title>
</head>

<div class="jumbotron">
	<div class="container">
		<p class="nickName">${loginUser.userId}</p>
		<div class="row-fluid">
			<div class="span4">
				<input id="fbLogoutBtn" class="btn btn-primary btn-large" style="display:none" type="button" value="페이스북 계정으로 로그아웃" />
			</div>
		</div>
	</div>
</div>
<div id="fb-root"></div>
<script src="//connect.facebook.net/en_US/all.js"></script>
<script>
  $(window).load(function() {
    // init the FB JS SDK
    FB.init({
      appId      : '129401280541462', // App ID from the App Dashboard
      status     : true, // check the login status upon init?
      cookie     : true, // set sessions cookies to allow your server to access the session?
      xfbml      : false  // parse XFBML tags on this page?
    });

    FB.getLoginStatus(function(response){
    	$("#fbLogoutBtn").show();
    });

    $("#fbLogoutBtn").click(function() {
		  FB.logout(function(response) {});
	});
  });
</script>
