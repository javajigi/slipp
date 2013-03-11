<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>

<div class="list-content">
	<div class="content-main">
		<section class="qna-list">
			<h1>전체목록</h1>
			<ul class="list">
			<c:forEach items="${questions.content}" var="each">
				<li>
					<div class="wrap">
						<div class="main">
							<strong class="subject">
								<a href="/questions/${each.questionId}">${sf:h(each.title)}</a>
							</strong>
							<c:if test="${each.denormalizedTags != ''}">
								<div class="tags">
									<i class="icon-tag" title="태그"></i>
									<span class="tag-list">
										<c:forEach items="${each.denormalizedTags}" var="tag">
											<span class="tag">${tag}</span>
										</c:forEach>
									</span>
								</div>
							</c:if>
							<div class="auth-info">
<<<<<<< HEAD
								<i class="icon-addtext"></i>
								<span class="time">
									<fmt:formatDate value="${each.createdDate}" pattern="yyyy-MM-dd HH:mm" />
=======
								<a href="${each.writer.profileUrl}" class="author">${each.latestParticipant.userId}</a>
								<span class="time">
									<fmt:formatDate value="${each.updatedDate}" pattern="yyyy-MM-dd HH:mm" />  
>>>>>>> develop
								</span>
								<a href="${each.writer.profileUrl}" class="author">${each.writer.userId}</a>
							</div>
							<div class="reply" title="댓글">
								<i class="icon-reply"></i>
								<span class="point">${each.answerCount + 1}</span>
							</div>
						</div>
					</div>
				</li>
			</c:forEach>
			</ul>
			<nav class="pager">
				<ul>
					<sl:pager page="${questions}" prefixUri="/questions"/>
				</ul>
			</nav>
		</section>
	</div>
	<div class="content-sub">
		<slipp:side-tags tags="${tags}"/>
	</div>
</div>
