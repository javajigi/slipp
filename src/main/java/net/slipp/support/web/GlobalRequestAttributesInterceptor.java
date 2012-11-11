package net.slipp.support.web;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.slipp.support.security.SessionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class GlobalRequestAttributesInterceptor extends HandlerInterceptorAdapter {
	private static final Logger log = LoggerFactory.getLogger(GlobalRequestAttributesInterceptor.class);
	
	@Resource (name = "sessionService")
	private SessionService sessionService;
	
	@Value("#{applicationProperties['environment']}")
	private String environment;
	
	@PostConstruct
	public void afterPropertiesSet() {
		log.info("current environment : {}", environment);
		Assert.notNull(environment, "Environment cann't be null!");
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setAttribute("loginUser", sessionService.getLoginUser());
		
		if (isDevelopEnvironment()) {
			runStylus();	
		}
		return super.preHandle(request, response, handler);
	}

	private boolean isDevelopEnvironment() {
		return "DEVELOPMENT".equals(environment);
	}
	
	private static void runStylus() throws IOException {
		File file = new File("./webapp/WEB-INF/");
		ProcessBuilder pb = new ProcessBuilder(file.getAbsolutePath() + "/styl.sh");
		pb.directory(new File(file.getAbsolutePath()));
		pb.start();
		log.debug("run stylus");
	}
}
