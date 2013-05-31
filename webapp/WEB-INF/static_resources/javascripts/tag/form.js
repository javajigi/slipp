$(document).ready(function() {
    $.validator.addMethod("duplicateEmail", function(value, element, param) {
    	var validator = this;
    	var email = $(param).val();
    	
		$.ajax({
			url: '/api/users/duplicateEmail',
			type: 'POST',
			data: { email: email },
			success: function(response) {
				if( !response ) {
					return true;
				}
				var errors = {};
				errors[element.name] = SL10N.User.duplicateEmail(email);
				validator.showErrors(errors);
				return false;
			}, 
			dataType: 'json',
			async: false
		});
		
		return true;
    }, '');
    
    $.validator.addMethod("duplicateTag", function(value, element, param) {
    	var validator = this;
    	var tagName = $(param).val();
    	
		$.ajax({
			url: '/api/tags/duplicateTag',
			type: 'POST',
			data: { name: tagName },
			success: function(response) {
				if( !response ) {
					return true;
				}
				var errors = {};
				errors[element.name] = SL10N.Tag.duplicateTag(tagName);
				validator.showErrors(errors);
				return false;
			}, 
			dataType: 'json',
			async: false
		});
		
		return true;
    }, '');
	
    $.validator.addMethod("validateEmail", function(value, element, param) {
    	var validator = this;
    	var emailAddress = $(param).val();
    	var regexp = /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i;
    	
    	return this.optional(element) || regexp.test(emailAddress);
    });
	
	$("#tag").validate({	
		rules: {
			email: {
				required: true,
				validateEmail: "#email",
				duplicateEmail: "#email"
			},
			name: {
				required: true,
				minlength: 2,
				maxlength: 20,
				duplicateTag: "#name"
			}
		},
		messages: {
			email: {
				required: SL10N.User.requiredEmail(),
				validateEmail: SL10N.User.invalidEmailFormat()
			},
			name: {
				required: SL10N.Tag.requiredName(),
				minlength: SL10N.Tag.minLenTag(2),
				maxlength: SL10N.Tag.maxLenTag(20)
			}
		}
	});	
});
