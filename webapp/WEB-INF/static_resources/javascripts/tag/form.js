$(document).ready(function() {
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
				maxlength: 50,
				duplicateTag: "#name"
			},
			description: {
				maxlength: 1000
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
				maxlength: SL10N.Tag.maxLenTag(50)
			},
			description: {
				maxlength: SL10N.Tag.maxLenDescription(1000)
			}
		}
	});	
});
