<%@ tag language="java" pageEncoding="UTF-8"%><%@ tag body-content="empty" %><%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@
taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@
taglib prefix="sec" uri="http://www.springframework.org/security/tags"%><%@
taglib prefix="sf" uri="http://slipp.net/functions"%><%@
taglib prefix="sl" uri="http://www.slipp.net/tags"%><%@
taglib prefix="slipp" tagdir="/WEB-INF/tags" %><%@
attribute name="each" required="true" rtexprvalue="true" type="net.slipp.domain.qna.Answer" description=" 목록"%><%@
attribute name="isBest" required="true" rtexprvalue="true" type="java.lang.Boolean" description=" 목록" %>

<li <c:if test="${isBest}">class="best"</c:if>>
	<div class="scoreArea" style="float:left;width: 40px;margin-top: 14px;text-align: center">
		<span style="color:#808185;font-weight: bold;font-size: 240%;"></span>
	</div>
	<div class="auth-info">
		<div class="author-thumb">
			<img src='${sf:stripHttp(each.writer.imageUrl)}' class="user-thumb" alt="" />
		</div>
		<div class="author-text">
			<a href="${sf:stripHttp(each.writer.profileUrl)}" class="author-name">${each.writer.userId}</a>
			<span class="time">
				<fmt:formatDate value="${each.createdDate}" pattern="yyyy-MM-dd HH:mm" />
			</span>
			<div class="likeAnswerBtn like" data-answer-id="${each.answerId}">
				<span class="star">★</span><strong class="like-count">${each.sumLike}</strong>
			</div>
		</div>
	</div>
	<div class="doc">
 		<div class="text">${sf:wiki(each.contents)}</div>
		<div class="util">
			<a class="likeAnswerBtn btn btn-like" data-answer-id="${each.answerId}" href="#" alt="${each.sumLike}"><span class="star">★</span> 공감</a>
			<c:if test="${sf:isWriter(each.writer, loginUser)}">
			<a class="deleteAnswerBtn btn btn-danger" data-answer-id="${each.answerId}" href="#">삭제</a>
			</c:if>
			<sec:authorize access="hasRole('ROLE_USER')">
			<a class="recommentAnswerBtn btn btn-info" data-answer-id="${each.answerId}" data-answer-user-id="@${each.writer.userId}" href="#">언급하기</a>
			</sec:authorize>
		</div>	
	</div>
</li>
