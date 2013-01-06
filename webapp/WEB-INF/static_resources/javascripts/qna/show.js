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

		if ( confirm('정말 삭제하시겠습니까?') ) {
			$deleteAnswerForm.attr("action", deleteAnswerUrl);
			$deleteAnswerForm.submit();
		}

		return false;
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
	
	setShowRealSizeImg();

	function replaceNicknames(){
		$('div.doc div.text').each(function(){
			var cont = $(this).html();
			for (var key in nickNames) {
				cont = cont.replace(new RegExp('@' + key, 'gi'), '<a href="'+nickNames[key]+'">'+key+'</a>');
			}
			
			$(this).html(cont);
		});
	}
	function addNickNames(){
		$('.author-name').each(function(){
			nickNames[$(this).text()] = $(this).attr('href');
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
	function setShowRealSizeImg() {
		var images = $('div.doc div.text img');
		var imageUrl = images.attr('src');

		images.wrap('<a href="'+imageUrl+'" target="_blank"></a>');
	}
	$(".likeAnswerBtn").on("click", function(){
		var answerId = $(this).data("answer-id");
		var $form = $('#likeAnswerForm');
		$form.attr("action", $form.attr("action")+"/"+answerId+"/like");
		$('#likeAnswerForm').submit();
	});
});