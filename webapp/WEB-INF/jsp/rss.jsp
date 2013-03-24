<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" 
%><?xml version="1.0" encoding="UTF-8"?> 
<rss version="2.0">
	<channel>
		<title>지속가능한 삶, 프로그래밍, 프로그래머(SLiPP)</title>
		<link>http://www.slipp.net/</link>
		<description>지속가능한 삶, 프로그래밍, 프로그래머(sustainable life, programming, programmer)</description>
		<language>ko</language>
		<pubDate><fmt:formatDate pattern="EEE, dd MMM yyyy HH:mm:ss Z" value="${now}"/></pubDate>
		<generator>slipp.net(http://www.slipp.net/)</generator>
		<image>
			<title>지속가능한 삶, 프로그래밍, 프로그래머(SLiPP)</title>
			<url><![CDATA[http://cfile23.uf.tistory.com/image/20637F134BC7B18E2E34A8]]></url>
			<link>http://www.slipp.net/</link>
			<description></description>
		</image>
		<c:forEach items="${pages}" var="page">
		<item>
			<title><![CDATA[${page.title}]]></title>
			<link>http://www.slipp.net/wiki/pages/viewpage.action?pageId=${page.pageId}</link>
			<description><![CDATA[${page.shortContents}]]></description>
			<author>slipp</author>
			<guid>http://www.slipp.net/wiki/pages/viewpage.action?pageId=${page.pageId}</guid>
			<comments>http://www.slipp.net/wiki/pages/viewpage.action?pageId=${page.pageId}</comments>
			<pubDate><fmt:formatDate pattern="EEE, dd MMM yyyy HH:mm:ss Z" value="${page.creationDate}"/></pubDate>
		</item>
		</c:forEach>
	</channel>
</rss>
