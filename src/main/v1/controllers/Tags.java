package controllers;

import java.util.List;

import models.Thread;
import models.ThreadTag;
import models.Tag;
import play.Logger;
import play.mvc.Controller;
import play.mvc.With;
import supports.web.Check;
import supports.web.GAESecure;
import supports.web.Role;

@With(GAESecure.class)
public class Tags extends Controller {
	public static void findsByName(String name) {
		List<Tag> tags = Tag.findsByName(name);
		renderJSON(tags);
	}
	
	@Check(Role.ROLE_ADMIN_USER)
	public static void init() {
		create("개발일지");
		create("java");
		create("자바", "java");
		create("javascript");
		create("자바스크립트", "javascript");
		create("jquery");
		create("android");
		create("안드로이드", "android");
		create("mysql");
		create("oracle");
		create("mssql");
		create("sql");
		create("html");
		create("css");
		create("xml");
		create("ajax");
		create("ruby");
		create("루비", "ruby");
		create("ruby-on-rails");
		create("windows");
		create("윈도우즈", "windows");
		create("linux");
		create("리눅스", "linux");
		create("unix");
		create("유닉스", "unix");
		create("mac");
		create("맥", "mac");
		create("eclipse");
		create("이클립스", "eclipse");
		create("ant");
		create("앤트", "ant");
		create("maven");
		create("메이븐", "maven");
		create("json");
		create("apache");
		create("아파치", "apache");
		create("tomcat");
		create("톰캣", "tomcat");
		create("svn");
		create("git");
		create("cvs");
		create("play");
		create("spring");
		create("스프링", "spring");
		create("struts");
		create("스트럿츠", "struts");
		create("struts2");
		create("스트럿츠2", "struts2");
		create("hibernate");
		create("하이버네이트", "hibernate");
		create("oop");
		create("file");
		create("string");
		create("mvc");
		create("jquery-ui");
		create("http");
		create("google-app-engine");
		create("mybatis");
		create("ibatis", "mybatis");
		create("class");
		create("jsp");
		create("servlets");
		create("서블릿", "servlets");
		create("function");
		create("caching");
		create("testing");
		create("테스트", "testing");
		create("unit-testing");
		create("단위테스트", "unit-testing");
		create("web");
		create("웹", "web");
		create("gwt");
		create("scala");
		create("rest");
		create("server");
		create("서버", "server");
		create("deployment");
		create("object");
		create("객체", "object");
		create("plugins");
		create("플러그인", "plugins");
		create("jpa");
		create("orm");
		create("logging");
		create("security");
		create("scripting");
		create("jquery-ajax");
		Threads.list(1);
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
}