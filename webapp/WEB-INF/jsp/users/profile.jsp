<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>로그인 :: SLiPP</title>
</head>

<img class="user-thumb" src="${sf:stripHttp(socialUser.imageUrl)}" width="80" height="80" alt="" />

display name : ${socialUser.displayName} 

<h2>Answers</h2>


<h2>Questions</h2>

