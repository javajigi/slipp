$(document).ready(function()	{
	$('#contents').markItUp(mySettings);
	
	$("#answersForm").validate({
		rules: {
			contents: "required"
		},
		messages: {
			contents: "내용을 입력하세요."
		}
	});
});