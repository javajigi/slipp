<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>

<div class="list-content">
	<div class="content-main">
		<section class="qna-list">
			<slipp:header />
			<c:if test="${currentTag.requestedTag }">
			<c:set var="tagInfo" value="${currentTag.tagInfo}"/>
			<c:if test="${currentTag.connectGroup}">
			연결 SNS : <a href="${tagInfo.groupUrl}" target="_blank">${tagInfo.groupUrl}</a><br/>
			</c:if>
			태그설명 : ${tagInfo.description}	
			</c:if>
			<ul class="list">
			<c:forEach items="${questions.content}" var="each">
				<slipp:list each="${each}"/>
			</c:forEach>
			</ul>
			<nav class="pager">
				<ul>
					<sl:pager page="${questions}" prefixUri="/questions/tagged/${currentTag.name}"/>
				</ul>
			</nav>
		</section>
	</div>
	<div class="content-sub">
		<slipp:side-tags tags="${tags}"/>
	</div>
</div>
