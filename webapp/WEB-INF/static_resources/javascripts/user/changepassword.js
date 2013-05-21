$(document).ready(function() {
    $.validator.addMethod("passwordConfirm", function (value, element, param) {
        var password = $(param).val();
        if (!password) {
            return true;
        }
        
        if( password !== value ) {
            return false;
        }
        
        return true;
    }, SL10N.User.confirmPassword());	
	
	$("#password").validate({
		rules: {
			oldPassword: {
				required: true
			},
			newPassword: {
				required: true
			},
			newPasswordConfirm: {
				required: true,
				passwordConfirm: "#newPassword"
			}
		},
		messages: {
			oldPassword: {
				required: SL10N.User.requiredOldPassword()
			},
			newPassword:  {
				required: SL10N.User.requiredNewPassword()
			},
			newPasswordConfirm: {
				required: SL10N.User.requiredNewPasswordConfirm(),
				passwordConfirm: SL10N.User.confirmPassword()
			}
		}
	});
});