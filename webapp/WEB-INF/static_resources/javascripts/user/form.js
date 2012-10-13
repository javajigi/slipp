$(document).ready(function() {
	$("#signUpForm").validate({
		rules: {
			username: "required"
		},
		messages: {
			username: "SLiPP에서 사용할 닉네임을 입력하세요."
		}
	});
});