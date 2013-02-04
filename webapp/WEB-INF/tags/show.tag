<%@ tag language="java" pageEncoding="UTF-8"%><%@ tag body-content="empty" %><%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@
taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@
taglib prefix="sf" uri="http://slipp.net/functions"%><%@
attribute name="question" required="true" rtexprvalue="true" type="net.slipp.domain.qna.Question" description="질문"%>

			<div class="content">
				<article class="article">
					<div class="wrap">
						<div class="auth-info">
							<div class="author-thumb">
								<img src='${sf:stripHttp(question.writer.imageUrl)}' class="user-thumb" alt="" />
							</div>
							<div class="author-text">
								<a href="${sf:stripHttp(question.writer.profileUrl)}" class="author-name">${question.writer.userId}</a>
								<span class="time">
									<fmt:formatDate value="${question.createdDate}" pattern="yyyy-MM-dd HH:mm" />
								</span>
							</div>
						</div>
						<div class="doc">
							<h1 class="subject">${sf:h(question.title)}</h1>
							<div class="tags">
								<ul>
								<c:forEach items="${question.pooledTags}" var="tag">
									<li>
										<a href="/questions/tagged/${tag.name}" class="tag">${tag.name}</a>
									</li>
								</c:forEach>
								</ul>
							</div>
							<div class="text">${sf:wiki(question.contents)}</div>
							<div class="share">
								<div class="facebook sns">
									<div id="fb-root"></div>
									<script src="https://connect.facebook.net/en_US/all.js#xfbml=1"></script>
									<fb:like href="${slippUrl}/questions/${question.questionId}"
									git	send="true" layout="button_count" width="100" show_faces="true"
										font=""></fb:like>
								</div>
								<div class="googleplus sns">
									<g:plusone></g:plusone>
								</div>
								<div class="twitter sns">
									<a href="https://twitter.com/share" class="twitter-share-button"
										data-count="horizontal">Tweet</a>
									<script type="text/javascript"
										src="https://platform.twitter.com/widgets.js"></script>
								</div>
							</div>
							<div class="util">
								<c:if test="${sf:isWriter(question.writer, loginUser)}">
								<a id="updateQuestionBtn" href="/questions/${question.questionId}/form" class="btn btn-primary">수정</a>
								<a id="deleteQuestionBtn" href="#" class="btn btn-danger">삭제</a>
								</c:if>	
								<a href="/questions"><button class="btn">목록으로</button></a>
							</div>
						</div>
					</div>
				</article>
				<form id="deleteQuestionForm" action="/questions/${question.questionId}" method="POST" class="flyaway">
					<input type="hidden" name="_method" value="DELETE" />
				</form>
			</div>
