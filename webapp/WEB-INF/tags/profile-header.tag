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
<<<<<<< HEAD
			<div class="person-info-join">
				<span class="person-info-label">가입일</span>
				<fmt:formatDate value="${socialUser.createDate}" pattern="yyyy-MM-dd HH:mm" />
			</div>
			<div class="person-info-sns">
				<span class="person-info-label">SNS</span>
				<a href=""><i class="foundicon-facebook"></i></a>
				<a href=""><i class="foundicon-twitter"></i></a>
				<a href=""><i class="foundicon-google-plus"></i></a>
			</div>
		</div>
		<c:if test="${sf:isWriter(socialUser, loginUser)}">
		<div class="person-info-util">
			<a href="/users/changepassword/${socialUser.id}">비밀번호 변경하기</a>
			<span class="bar"> | </span>
			<a href="/users/changenickname/${socialUser.id}">닉네임 변경하기</a>
=======
			<c:if test="${sf:isWriter(socialUser, loginUser) && loginUser.SLiPPUser}">
			<a href="/users/${socialUser.id}/changepassword" class="person-info-link-to-change-pw">비밀번호 변경하기</a> 
			&nbsp;
			</c:if>
			<c:if test="${sf:isWriter(socialUser, loginUser)}">
			<a href="/users/${socialUser.id}/form">개인정보 변경하기</a>
			</c:if>
			<strong>가입일:</strong> <fmt:formatDate value="${socialUser.createDate}" pattern="yyyy-MM-dd HH:mm" />
>>>>>>> develop
		</div>
		</c:if>
	</div>
