$(document).ready(function() {
    function createJoinOptions() {
        var userOptions = {
        		rules: {
        			email: {
        				required: true,
        				validateEmail: "#email",
        				duplicateEmail: "#email"
        			},
        			userId: {
        				required: true,
        				minlength: 2,
        				maxlength: 20,
        				duplicateUserId: "#userId"
        			}
        		},
        		messages: {
        			email: {
        				required: SL10N.User.requiredEmail(),
        				validateEmail: SL10N.User.invalidEmailFormat()
        			},
        			userId: {
        				required: SL10N.User.requiredUserId(),
        				minlength: SL10N.User.minLenUserId(2),
        				maxlength: SL10N.User.maxLenUserId(20)
        			}
        		}
        	};
        return userOptions;
    };
    
    function createModifyUserOptions() {
    	var userOptions = createJoinOptions();
        userOptions.submitHandler = function(form) {
            window.alert(SL10N.Global.relogin());
            form.submit();
        };
        
        return userOptions;
    };
    
    $("#user").validate(createJoinOptions());
    
    $("#modifyUser").validate(createModifyUserOptions());
	
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
				required: SL10N.User.requiredEmail()
			},
			authenticationPassword:  {
				required: SL10N.User.requiredPassword()
			}
		}
	});	
});