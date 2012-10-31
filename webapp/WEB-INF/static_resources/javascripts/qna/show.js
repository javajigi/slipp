$(document).ready(function(){
	var nickNames = new Array();
	
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
	
	$(".commentList").hover(function(){
		$(this).find(".commBtn").show('fast');
	}, function(){
		$(this).find(".commBtn").hide('fast');
	});
	$(".recommentAnswerBtn").on('click', function(){
		var orgUserId = $(this).data('answer-user-id');
		var contents = arroundSpace( $('#contents').val(), orgUserId );

		$('#contents').val('').focus().val(contents);
		$('#contents').focus();
		$('html, body').animate({scrollTop: $(document).height()}, 'slow');
		return false;
	});
	
	addNickNames();
	
	replaceNicknames();
	
	function replaceNicknames(){
		$('.cont').each(function(){
			var pattern;
			var cont = $(this).text();
			for (var key in nickNames) {
				cont = cont.replace(key, '<a href="'+nickNames[key]+'">'+key+'</a>');
			}
			
			$(this).html(cont);
		});
	}
	function addNickNames(){
		$('.tester').each(function(){
			nickNames[$(this).text()] = $(this).find('a').attr('href');
		});
	}
	function arroundSpace(contents, orgUserId){
		if( $.trim(contents).length > 0) {
			contents = contents +" "+orgUserId +" ";
		}else{
			contents = contents + orgUserId;
		}
		return contents;
	}
	
});