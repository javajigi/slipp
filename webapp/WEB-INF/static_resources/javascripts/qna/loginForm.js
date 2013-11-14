$(document).ready(function(){
	hideLoginForm();
	showLoginForm();
	answerLogoutUserTo();
	
	function hideLoginForm() {
		$('.choose-login-type').hide();
	}
	
	function showLoginForm() {
		$('#contents').focusin(function() {
			$('.choose-login-type').slideDown('slow');
		});
	}
	
	function answerLogoutUserTo() {
		var url = '/api/questions/' + questionId + '/answers/logoutuser';
		
		$('.choose-login-type button[type="submit"]').click(function(e) {
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