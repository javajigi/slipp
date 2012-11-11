package net.slipp.support.web;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.slipp.support.security.SessionService;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class GlobalRequestAttributesInterceptor extends HandlerInterceptorAdapter {
	@Resource (name = "sessionService")
	private SessionService sessionService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setAttribute("loginUser", sessionService.getLoginUser());
		runStylus();
		return super.preHandle(request, response, handler);
	}
	
	private static void runStylus() throws IOException {
		File file = new File("./webapp/WEB-INF/");
		ProcessBuilder pb = new ProcessBuilder(file.getAbsolutePath() + "/styl.sh");
		pb.directory(new File(file.getAbsolutePath()));
		pb.start();
		System.out.println("run stylus");
	}
}
