$(document).ready(function() {
    $.validator.addMethod("duplicateUserId", function(value, element, param) {
    	var validator = this;
    	var userId = $(param).val();
    	
		$.post('/api/users/duplicateUserId',
				{ userId: userId },
				function(response) {
					if( !response ) {
						return true;
					}
					
					var errors = {};
					errors[element.name] = AL10N.User.duplicateUserId(userId);
					validator.showErrors(errors);
					return false;
				}, 'json'
		);
		
		return true;
    });
	
	$("#signUpForm").validate({
		rules: {
			userId: {
				required: true,
				minlength: 4,
				maxlength: 12,
				regexp: /^[0-9a-zA-Z]{4,12}$/,
				duplicateUserId: "#userId"
			},
			nickName:  {
				required: true,
				minlength: 2,
				maxlength: 12
			}
		},
		messages: {
			userId: {
				required: AL10N.User.requiredUserId(),
				minlength: AL10N.User.minLenUserId(4),
				maxlength: AL10N.User.maxLenUserId(12),
				regexp: AL10N.User.invalidUserIdFormat()
			},
			nickName:  {
				required: AL10N.User.requiredNickName(),
				minlength: AL10N.User.minLenNickName(2),
				maxlength: AL10N.User.maxLenNickName(12)
			}
		}
	});
});