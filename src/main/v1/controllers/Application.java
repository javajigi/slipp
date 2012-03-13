package controllers;

import models.Tag;
import play.Logger;
import play.modules.gae.GAE;
import play.mvc.Controller;
import play.mvc.With;
import supports.web.Check;
import supports.web.GAESecure;
import supports.web.Role;

@With(GAESecure.class)
public class Application extends Controller {
	static {
		create("개발일지");
		create("java");
		create("자바", "java");
		create("javascript");
		create("자바스크립트", "javascript");
		create("jquery");
		
		Logger.info("Slipp initialization success!!");
	}
	
	private static void create(String name) {
		if (Tag.findByName(name)==null) {
			new Tag(name).insert();
			Logger.debug(name + " tag inserted!!");
		}
	}
	
	private static void create(String name, String parentName) {
		Tag parent = Tag.findByName(parentName);
		if (Tag.findByName(name)==null) {
			new Tag(name, parent).insert();
			Logger.debug(name + " tag inserted!!");
		}
	}
	
    public static void keepalived() {
    	render();
    }
    
    public static void about() {
    	render();
    }
    
    public static void code() {
    	render();
    }

    @Check(Role.ROLE_USER)
    public static void login() {
    	Threads.list(1);
    }
 
    public static void logout() {
    	GAE.logout("Threads.list");
    }
}