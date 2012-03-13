package models;

import play.modules.gae.GAE;

import com.google.appengine.api.users.User;

public class Auth {
	public static boolean isLoggedIn() {
        return GAE.isLoggedIn();
    }
	
	public static boolean isUserAdmin() {
		return GAE.isAdmin();
	}
	
    public static String getEmail() {
        return GAE.getUser().getEmail();
    }
    
    public static User getUser() {
        return GAE.getUser();
    }
    
    public static SlippUser getSlippUser() {
    	if (!isLoggedIn()) {
    		return null;
    	}

    	User user = getUser();
    	return new SlippUser(user.getEmail(), user.getNickname());
    }
    
    public static void login(String action) {
        GAE.login(action);
    }
 
    public static void logout(String action) {
        GAE.logout(action);
    }
}
