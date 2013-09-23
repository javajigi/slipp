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
			nickName:  {
				required: true,
				minlength: 2,
				maxlength: 12
			}
		},
		messages: {
			nickName:  {
				required: SL10N.User.requiredUserId(),
				minlength: SL10N.User.minLenUserId(2),
				maxlength: SL10N.User.maxLenUserId(12)
			}
		}
	});
});