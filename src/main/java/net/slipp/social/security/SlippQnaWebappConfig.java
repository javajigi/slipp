package net.slipp.social.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

@Configuration
public class SlippQnaWebappConfig {
	@Bean
	public ConnectionFactoryRegistry connectionFactoryRegistry() {
		return new ConnectionFactoryRegistry();
	}


	@Bean
	public DefaultAnnotationHandlerMapping handlerMapping() throws Exception {
		DefaultAnnotationHandlerMapping mapping = new DefaultAnnotationHandlerMapping();
		return mapping;
	}
	

	@Bean
	public AnnotationMethodHandlerAdapter handlerAdapter() throws Exception {
		AnnotationMethodHandlerAdapter mapping = new AnnotationMethodHandlerAdapter();
		return mapping;
	}

}
