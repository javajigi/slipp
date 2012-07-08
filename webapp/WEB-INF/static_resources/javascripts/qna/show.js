$(document).ready(function(){
	$('#contents').markItUp(mySettings);
	
	$("#answersForm").validate({
		rules: {
			contents: "required"
		},
		messages: {
			contents: "내용을 입력하세요."
		}
	});
	
	$("#deleteQuestionBtn").click(function() {
		$("#deleteQuestionForm").submit();
		return false;
	});
	
	$deleteAnswerBtn = $(".deleteAnswerBtn");
	$deleteAnswerForm = $("#deleteAnswerForm");
	var deleteAnswerUrlPrefix = $deleteAnswerForm.attr("action");
	$deleteAnswerBtn.click(function() {
		var deleteAnswerUrl = deleteAnswerUrlPrefix + $(this).data("answerId");
		$deleteAnswerForm.attr("action", deleteAnswerUrl);
		$deleteAnswerForm.submit();
		return false;
	});
});