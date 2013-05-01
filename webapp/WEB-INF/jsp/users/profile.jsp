<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
	<title>${socialUser.displayName} 공간 :: SLiPP</title>
</head>

<img class="user-thumb" src="${sf:stripHttp(socialUser.imageUrl)}" width="80" height="80" alt="" />

display name : ${socialUser.displayName} 

<a href="/users/changepassword/${socialUser.id}">비밀번호 변경하기</a>

<h2>Answers</h2>


<h2>Questions</h2>

