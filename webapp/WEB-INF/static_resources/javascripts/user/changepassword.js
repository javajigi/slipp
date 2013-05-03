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
    }, AL10N.User.confirmPassword());	
	
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
				required: AL10N.User.requiredOldPassword()
			},
			newPassword:  {
				required: AL10N.User.requiredNewPassword()
			},
			newPasswordConfirm: {
				required: AL10N.User.requiredNewPasswordConfirm(),
				passwordConfirm: AL10N.User.confirmPassword()
			}
		}
	});
});