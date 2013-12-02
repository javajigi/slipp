<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<head>
<title>태그관리 :: SLiPP</title>
</head>

<section class="taglist-content">
	<h1>태그 목록</h1>
	<div class="content-main">
		<slipp:tags tags="${tags}" admin="false"/>
		<nav class="pager">
			<ul>
				<sl:pager page="${tags}" prefixUri="/tags"/>
			</ul>
		</nav>
	</div>
	<div class="content-sub">
	</div>
</section>