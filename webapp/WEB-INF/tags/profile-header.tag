<%@ tag language="java" pageEncoding="UTF-8"%><%@ tag body-content="empty"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"
%><%@ taglib prefix="sf" uri="http://slipp.net/functions"
%><%@ attribute name="socialUser" required="true" rtexprvalue="true" type="net.slipp.domain.user.SocialUser" description="사용자"
%>
	<div class="person-info">
		<c:choose>
		<c:when test="${socialUser.SLiPPUser}">
		<a href="http://ko.gravatar.com/" target="_blank"><img class="person-info-thumb" src="${sf:stripHttp(socialUser.imageUrl)}" width="80" height="80" alt="" /></a>
		</c:when>
		<c:otherwise>
		<a href="${socialUser.profileUrl}" target="_blank"><img class="person-info-thumb" src="${sf:stripHttp(socialUser.imageUrl)}" width="80" height="80" alt="" /></a>		
		</c:otherwise>
		</c:choose>
		<div class="person-info-text">
			<h1 class="person-info-name"><a href="${socialUser.url}">${socialUser.userId}</a></h1>
			<div class="person-info-join">
				<span class="person-info-label">가입일</span>
				<fmt:formatDate value="${socialUser.createDate}" pattern="yyyy-MM-dd HH:mm" />
			</div>
			<c:if test="${not socialUser.SLiPPUser}">
			<div class="person-info-sns">
				<span class="person-info-label">SNS</span>
				<a href="${socialUser.profileUrl}" target="_blank">
					<c:if test="${socialUser.providerId == 'facebook'}">
					<i class="foundicon-facebook"></i>
					</c:if>
					<c:if test="${socialUser.providerId == 'twitter'}">
					<i class="foundicon-twitter"></i>
					</c:if>
					<c:if test="${socialUser.providerId == 'google'}">
					<i class="foundicon-google-plus"></i>
					</c:if>					
				</a>
			</div>
			</c:if>
		</div>
		<c:if test="${sf:isWriter(socialUser, loginUser)}">
		<div class="person-info-util">
			<c:if test="${socialUser.SLiPPUser}">
			<a href="/users/${socialUser.id}/changepassword">비밀번호 변경하기</a>
			<span class="bar"> | </span>
			</c:if>
			<a href="/users/${socialUser.id}/form">개인정보 변경하기</a>
		</div>
		</c:if>
	</div>
