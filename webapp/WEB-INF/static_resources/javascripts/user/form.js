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
					errors[element.name] = SL10N.User.duplicateUserId(userId);
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
				required: SL10N.User.requiredUserId(),
				minlength: SL10N.User.minLenUserId(4),
				maxlength: SL10N.User.maxLenUserId(12),
				regexp: SL10N.User.invalidUserIdFormat()
			},
			nickName:  {
				required: SL10N.User.requiredNickName(),
				minlength: SL10N.User.minLenNickName(2),
				maxlength: SL10N.User.maxLenNickName(12)
			}
		}
	});
});