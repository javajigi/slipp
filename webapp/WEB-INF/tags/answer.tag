<%@ tag language="java" pageEncoding="UTF-8"%><%@ tag body-content="empty" %><%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@
taglib prefix="sec" uri="http://www.springframework.org/security/tags"%><%@
taglib prefix="sf" uri="http://slipp.net/functions"%><%@
taglib prefix="sl" uri="http://www.slipp.net/tags"%><%@
taglib prefix="slipp" tagdir="/WEB-INF/tags" %><%@
attribute name="each" required="true" rtexprvalue="true" type="net.slipp.domain.qna.Answer" description=" 목록"%><%@
attribute name="isBest" required="true" rtexprvalue="true" type="java.lang.Boolean" description=" 목록" %>
<div class="commentList" <c:if test="${isBest}">style="background-color: #efefaa"</c:if>>
	<div class="scoreArea" style="float:left;width: 40px;margin-top: 14px;text-align: center">
		<span style="color:#808185;font-weight: bold;font-size: 240%;">${each.sumLike}</span>
	</div>
	<div class="nickArea">
		<p class='prphoto'><img src='${sf:stripHttp(each.writer.imageUrl)}' /></p>
		<div class="nickname">
			<div class="tester"><span class='lv'><a href="${sf:stripHttp(each.writer.profileUrl)}">${each.writer.userId}</a></span></div>
		</div>
	</div>
	<div class="list">
		<div class="cont">${sf:wiki(each.contents)}</div>
		<div class="regDate"><fmt:formatDate value="${each.createdDate}" pattern="yyyy-MM-dd HH:mm" /></div>
	</div>
	<div class="commBtn" style="display: none;">
		<c:if test="${sf:isWriter(each.writer, loginUser)}">
		<a class="deleteAnswerBtn" data-answer-id="${each.answerId}" href="#">삭제</a>
		 | 
		</c:if>
		
		<sec:authorize access="hasRole('ROLE_USER')">
		<a class="recommentAnswerBtn" data-answer-id="${each.answerId}" data-answer-user-id="@${each.writer.userId}" href="#">댓글</a>
		 | 
		<a class="likeAnswerBtn" data-answer-id="${each.answerId}" href="#" alt="${each.sumLike}">좋아요</a>
		</sec:authorize>
	</div>
</div>
