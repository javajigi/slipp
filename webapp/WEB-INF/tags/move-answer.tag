<%@ tag language="java" pageEncoding="UTF-8"%><%@ tag body-content="empty" %><%@
        taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@
        taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@
        taglib prefix="sec" uri="http://www.springframework.org/security/tags"%><%@
        taglib prefix="sf" uri="http://slipp.net/functions"%><%@
        taglib prefix="sl" uri="http://www.slipp.net/tags"%><%@
        taglib prefix="slipp" tagdir="/WEB-INF/tags" %><%@
        attribute name="each" required="true" rtexprvalue="true" type="net.slipp.domain.qna.Answer" description="답변"%>

<article class="article">
    <div class="article-header">
        <div class="article-header-thumb">
            <img src='${sf:stripHttp(each.writer.imageUrl)}' class="article-author-thumb" alt="" />
        </div>
        <div class="article-header-text">
            <a href="${sf:stripHttp(each.writer.url)}" class="article-author-name">${each.writer.userId}</a>
            <a href="#answer-${each.answerId}" class="article-header-time" title="퍼머링크">
                <fmt:formatDate value="${each.createdDate}" pattern="yyyy-MM-dd HH:mm" />
                <input type="checkbox" name="moveAnswers" value="${each.answerId}" />
            </a>
        </div>
    </div>
    <div class="article-doc comment-doc">
        ${sf:wiki(each.contents)}
    </div>
    <div class="article-value">
    </div>
</article>
