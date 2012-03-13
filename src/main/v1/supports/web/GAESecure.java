package supports.web;

import models.Auth;
import models.HasNotRoleException;
import play.mvc.Before;
import play.mvc.Controller;

public class GAESecure extends Controller {
	@Before
    static void checkAccess() throws Throwable {
		initLoginStatus();
		
		Check check = getActionAnnotation(Check.class);
		if (check != null) {
			check(check);
			return;
		}
		check = getControllerAnnotation(Check.class);
		if (check != null) {
			check(check);
			return;
		}
	}

	private static void initLoginStatus() {
		renderArgs.put("isUserLoggedIn", Auth.isLoggedIn());
        if(Auth.getUser() != null) {
        	renderArgs.put("isUserAdmin", Auth.isUserAdmin());
            renderArgs.put("user", Auth.getUser());
        } else {
        	renderArgs.put("isUserAdmin", false);
        }		
	}

	private static void check(Check check) throws HasNotRoleException {
		Role role = check.value();
		if (role == Role.ROLE_USER || role == Role.ROLE_ADMIN_USER) {
			if (!Auth.isLoggedIn()) {
				Auth.login(request.action);
			}	
		}
		
		if (role == Role.ROLE_ADMIN_USER) {
			if (!Auth.isUserAdmin()){
				Auth.login(request.action);
			}
		}
	}
}
