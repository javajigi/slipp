<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
<title>태그관리 :: SLiPP</title>
</head>

<div class="section-qna">
	<slipp:header type="2" />
	<slipp:tags tags="${tags}" admin="false"/>
	<div class="pagination pagination-centered">
		<ul>
			<sl:pager page="${tags}" prefixUri="/tags"/>
		</ul>
	</div>
</div>
