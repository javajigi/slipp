$(document).ready(function(){
	hideLoginForm();
	showLoginForm();
	answerLogoutUserTo();
	
	function hideLoginForm() {
		$('.qna-login-for-comment').hide();
	}
	
	function showLoginForm() {
		$('#contents').focusin(function() {
			$('.qna-login-for-comment').slideDown('slow');
		});
	}
	
	function answerLogoutUserTo() {
		var url = '/api/questions/' + questionId + '/answers/logoutuser';
		
		$('.qna-login-for-comment button[type="submit"]').click(function(e) {
			$that = $(this);
			e.preventDefault();
			$.post(
				url,
				{contents:$('#contents').val()},
				function(result) {
					$that.closest('form').submit();
				}
			)
		});
	}
});