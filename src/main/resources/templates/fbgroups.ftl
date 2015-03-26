<#ftl encoding='UTF-8'>
<section class="qna-connect-facebook">
	<h1>페이스북 연동</h1>
	<label class="qna-connect-facebook-item">
		<input id="connected1" name="connected" type="checkbox" value="true">
		<span>내 담벼락에 게시</span>
	</label>
	<h2>내 그룹에 게시</h2>
	<#list groups as each>
	<label class="qna-connect-facebook-item">
	<input type="checkbox" name="plainFacebookGroups" value="${each.id}::${each.name}" />
	<span>${each.name}</span>
	</label>
	</#list>
</section>
