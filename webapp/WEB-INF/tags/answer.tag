<%@ tag language="java" pageEncoding="UTF-8"%><%@ tag body-content="empty" %><%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@
taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@
taglib prefix="sec" uri="http://www.springframework.org/security/tags"%><%@
taglib prefix="sf" uri="http://slipp.net/functions"%><%@
taglib prefix="sl" uri="http://www.slipp.net/tags"%><%@
taglib prefix="slipp" tagdir="/WEB-INF/tags" %><%@
attribute name="each" required="true" rtexprvalue="true" type="net.slipp.domain.qna.Answer" description="답변"%><%@
attribute name="isBest" required="true" rtexprvalue="true" type="java.lang.Boolean" description="" %>

<article class="article<c:if test='${isBest}'> best-comment</c:if>" <c:if test="${not isBest}">id="answer-${each.answerId}"</c:if>>
	<c:if test="${isBest}">
		<span class="answer-best">best</span>
	</c:if>
	<div class="article-header">
		<div class="article-header-thumb">
			<img src='${sf:stripHttp(each.writer.imageUrl)}' class="article-author-thumb" alt="" />
		</div>
		<div class="article-header-text">
			<a href="${sf:stripHttp(each.writer.profileUrl)}" class="article-author-name">${each.writer.userId}</a>
			<a href="#answer-${each.answerId}" class="article-header-time" title="퍼머링크">
				<fmt:formatDate value="${each.createdDate}" pattern="yyyy-MM-dd HH:mm" />
				<i class="icon-link"></i>
			</a>
		</div>
	</div>
	<div class="article-doc">
		${sf:wiki(each.contents)}
		<div class="article-doc-util">
			<button type="button" class="btn-like-article" data-answer-id="${each.answerId}">
				<i class="icon-star"></i>
				<strong class="like-count">${each.sumLike}</strong>
				<span class="txt">공감</span>
			</button>
			<c:if test="${sf:isWriter(each.writer, loginUser)}">
				<a class="link-update-article" href="/questions/${question.questionId}/answers/${each.answerId}/form">수정</a>
				<form class="form-delete" action="/questions/${question.questionId}/answers/${each.answerId}" method="POST">
					<input type="hidden" name="_method" value="DELETE" />
					<button type="submit" class="link-delete-article">삭제</button>
				</form>
			</c:if>
			<sec:authorize access="hasRole('ROLE_USER')">
				<button type="button" class="btn-answer-article" data-answer-id="${each.answerId}" data-answer-user-id="@${each.writer.userId}">언급 &darr;</button>
			</sec:authorize>
		</div>
	</div>
</article>
