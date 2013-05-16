<%@ tag language="java" pageEncoding="UTF-8"%><%@ tag body-content="empty" %><%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@
taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@
taglib prefix="sf" uri="http://slipp.net/functions"%><%@
attribute name="question" required="true" rtexprvalue="true" type="net.slipp.domain.qna.Question" description="질문"%>

<article class="article">
	<div class="article-header">
		<div class="article-header-thumb">
			<img src='${sf:stripHttp(question.writer.imageUrl)}' class="article-author-thumb" alt="" />
		</div>
		<div class="article-header-text">
			<a href="${sf:stripHttp(question.writer.url)}" class="article-author-name">${question.writer.userId}</a>
			<a href="/questions/${question.questionId}" class="article-header-time" title="퍼머링크">
				<fmt:formatDate value="${question.createdDate}" pattern="yyyy-MM-dd HH:mm" />
				<i class="icon-link"></i>
			</a>
		</div>
	</div>
	<div class="article-doc">
		${sf:wiki(question.contents)}
	</div>
	<c:if test="${sf:isWriter(question.writer, loginUser)}">
		<div class="article-util">
			<ul class="list-util">
				<li>
					<a class="link-modify-article" href="/questions/${question.questionId}/form">수정</a>
				</li>
				<li>
					<form class="form-delete" action="/questions/${question.questionId}" method="POST">
						<input type="hidden" name="_method" value="DELETE" />
						<button class="link-delete-article" type="submit">삭제</button>
					</form>
				</li>
			</ul>
		</div>
	</c:if>
</article>
