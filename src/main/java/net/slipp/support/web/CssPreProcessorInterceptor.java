package net.slipp.support.web;

import java.io.File;
import java.io.StringWriter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CssPreProcessorInterceptor extends HandlerInterceptorAdapter {
	private static final Logger log = LoggerFactory.getLogger(GlobalRequestAttributesInterceptor.class);
	
	private static final String DEFAULT_WINDOWS_NAME = "Windows";
	
	@Value("#{applicationProperties['environment']}")
	private String environment;

	@PostConstruct
	public void afterPropertiesSet() {
		log.info("current environment : {}", environment);
		Assert.notNull(environment, "Environment cann't be null!");
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (isDevelopEnvironment()) {
			runStylus();	
		}
		return super.preHandle(request, response, handler);
	}

	private boolean isDevelopEnvironment() {
		return "DEVELOPMENT".equals(environment);
	}
	
	private void runStylus() {
		try {
			File file = new File("./webapp/WEB-INF/");
			ProcessBuilder pb = new ProcessBuilder(file.getAbsolutePath() + "/" + shellFileName());
			log.debug("stylus file path : {}", file.getAbsolutePath());
			pb.directory(new File(file.getAbsolutePath()));
			Process process = pb.start();
			StringWriter writer = new StringWriter();
			IOUtils.copy(process.getInputStream(), writer, "UTF-8");
			log.debug(writer.toString());
		} catch (Exception e) {
			log.warn("cannot find styl.sh file!");
		}
	}
	
	private String shellFileName() {
		if (isWindows()) {
			return "styl.bat";
		}
		
		return "styl.sh";
	}
	
	private boolean isWindows() {
		String osName = System.getProperty("os.name");
		return osName.contains(DEFAULT_WINDOWS_NAME);
	}
}
