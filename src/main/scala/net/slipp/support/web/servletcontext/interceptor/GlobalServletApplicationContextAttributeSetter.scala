package net.slipp.support.web.servletcontext.interceptor

import javax.servlet.ServletContext
import net.slipp.support.utils.ConvenientProperties
import org.springframework.beans.factory.InitializingBean
import org.springframework.web.context.ServletContextAware

/**
  * Servlet Application Context 전체적으로 사용되는 환경 변수를 지정한다. application scope에 저장된다.
  */
class GlobalServletApplicationContextAttributeSetter extends ServletContextAware with InitializingBean {
  private var servletContext: ServletContext = null
  private var applicationProperties: ConvenientProperties = null

  def setApplicationProperties(applicationProperties: ConvenientProperties) {
    this.applicationProperties = applicationProperties
  }

  def setServletContext(servletContext: ServletContext) {
    this.servletContext = servletContext
  }

  def afterPropertiesSet {
    populateApplicationContextAttributes
    addApplicationUrl
  }

  private def addApplicationUrl {
    servletContext.setAttribute("slippUrl", applicationProperties.get("application.url"))
    servletContext.setAttribute("facebookSlippUrl", applicationProperties.get("facebook.application.url"))
  }

  private def populateApplicationContextAttributes {
    servletContext.setAttribute("applicationProperties", applicationProperties)
  }
}
