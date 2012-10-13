<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"
%><%@include file="/WEB-INF/jsp/include/tags.jspf"%><!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="utf-8">
    <title><decorator:title default="SLiPP"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="자바지기">
    
    <link rel="shortcut icon" type="image/x-icon" href="${url:resource('/images/favicon.ico')}">
    <link href="${url:resource('/stylesheets/bootstrap.css')}" rel="stylesheet">
    <style>
      body {
        padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */
      }
    </style>
    <link href="${url:resource('/stylesheets/bootstrap-responsive.css')}" rel="stylesheet">
	<link href="${url:resource('/stylesheets/boards.css')}" rel="stylesheet">
    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/jquery-ui.min.js"></script>
	<decorator:head />
  </head>

  <body>
    <div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="/">지속가능한 삶, 프로그래밍, 프로그래머</a>
          <div class="nav-collapse">
            <ul class="nav nav-pills pull-right">
              <li><a href="${slippUrl}/questions">QnA</a></li>
              <li><a href="${slippUrl}/wiki">Wiki</a></li>
              <li><a href="https://github.com/javajigi/slipp/issues">Issues</a></li>
              <li><a href="${slippUrl}/code">Code</a></li>
              <li><a href="${slippUrl}/about">SLiPP.net</a></li>
              <sec:authorize access="!hasRole('ROLE_USER')">
              <li class="active loginBtn"><a href="/login">로그인</a></li>
              </sec:authorize>
              <sec:authorize access="hasRole('ROLE_USER')">
              <li class="active logoutBtn"><a href="/logout">로그아웃</a></li>
              </sec:authorize>              
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>
    
    <div class="container">
	  <decorator:body/>
    </div> <!-- /container -->

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