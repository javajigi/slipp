<#ftl encoding='UTF-8'>
<#list comments as comment>
<article class="article">
	<div class="article-header">
		<div class="article-header-thumb">
			<img src='//graph.facebook.com/${comment.userId()}/picture' class="article-author-thumb" alt="" />
		</div>
		<div class="article-header-text">
			<a href="//facebook.com/profile.php?id=${comment.userId()}" class="article-author-name">${comment.name()}</a><#if comment.groupId()??><span class="article-author-fbgroup">from <a href="//facebook.com/groups/${comment.groupId()}" target="_blank">${comment.groupName()}</a></span></#if>
			<div class="article-header-time">
				${comment.createdTime()?string("yyyy-MM-dd HH:mm")}
			</div>
		</div>
	</div>
	<div class="article-doc comment-doc">
		<p>${comment.getMessage()}</p>
	</div>
</article>
</#list>
