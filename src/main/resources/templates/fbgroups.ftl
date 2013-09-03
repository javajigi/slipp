<#ftl encoding='UTF-8'>
<input id="connected1" name="connected" type="checkbox" value="true"> 페이스북으로 전송하려면 체크하세요<br/>
태그와 연결할 페이스북 그룹<br/>
<#list groups as each>
<input type="checkbox" name="plainFacebookGroups" value="${each.groupId}::${each.name}" />${each.name}<br/>
</#list>
