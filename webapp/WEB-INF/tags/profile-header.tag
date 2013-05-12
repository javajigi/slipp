<%@ tag language="java" pageEncoding="UTF-8"%><%@ tag body-content="empty"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"
%><%@ taglib prefix="sf" uri="http://slipp.net/functions"
%><%@ attribute name="socialUser" required="true" rtexprvalue="true" type="net.slipp.domain.user.SocialUser" description="사용자"
%>
	<div class="person-info">
		<img class="person-info-thumb" src="${sf:stripHttp(socialUser.imageUrl)}" width="80" height="80" alt="" />
		<div class="person-info-text">
			<h1 class="person-info-name">${socialUser.displayName}</h1>
			<c:if test="${sf:isWriter(socialUser, loginUser)}">
			<a href="/users/changepassword/${socialUser.id}" class="person-info-link-to-change-pw">비밀번호 변경하기</a> 
			&nbsp;
			<a href="/users/changenickname/${socialUser.id}">닉네임 변경하기</a>
			</c:if>
			<strong>가입일:</strong> <fmt:formatDate value="${socialUser.createDate}" pattern="yyyy-MM-dd HH:mm" />
		</div>
	</div>
