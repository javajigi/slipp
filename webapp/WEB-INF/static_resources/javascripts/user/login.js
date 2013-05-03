$(document).ready(function() {
    $.validator.addMethod("regexp", function(value, element, regexp) {
    	return this.optional(element) || regexp.test(value);
    });
    
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
    
    $.validator.addMethod("validateEmail", function(value, element, param) {
    	var validator = this;
    	var emailAddress = $(param).val();
    	var regexp = /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i;
    	
    	return this.optional(element) || regexp.test(emailAddress);
    });    
	
	$("#user").validate({
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
				minlength: 3,
				maxlength: 12
			},
			email: {
				required: true,
				validateEmail: "#email"
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
				minlength: AL10N.User.minLenNickName(3),
				maxlength: AL10N.User.maxLenNickName(12)
			},
			email: {
				required: AL10N.User.requiredEmail(),
				validateEmail: AL10N.User.invalidEmailFormat()
			}
		}
	});
	
	$("#authentication").validate({
		rules: {
			authenticationId: {
				required: true
			},
			authenticationPassword:  {
				required: true
			}
		},
		messages: {
			authenticationId: {
				required: AL10N.User.requiredUserId()
			},
			authenticationPassword:  {
				required: AL10N.User.requiredPassword()
			}
		}
	});	
});