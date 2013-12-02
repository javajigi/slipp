package net.slipp.support.web.servletcontext.interceptor;

import javax.servlet.ServletContext;

import net.slipp.support.utils.ConvenientProperties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

/**
 * Servlet Application Context 전체적으로 사용되는 환경 변수를 지정한다. application scope에 저장된다.
 */
public class GlobalServletApplicationContextAttributeSetter implements ServletContextAware, InitializingBean {

	private ServletContext servletContext;

	private ConvenientProperties applicationProperties;

	public void setApplicationProperties(ConvenientProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		populateApplicationContextAttributes();
		addApplicationUrl();
	}

	private void addApplicationUrl() {
		servletContext.setAttribute("slippUrl", applicationProperties.get("application.url"));
		servletContext.setAttribute("facebookSlippUrl", applicationProperties.get("facebook.application.url"));
	}

	private void populateApplicationContextAttributes() {
		servletContext.setAttribute("applicationProperties", applicationProperties);
	}
}
