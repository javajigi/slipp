package net.slipp

import java.util.EnumSet
import javax.servlet.{DispatcherType, ServletContext}

import com.opensymphony.sitemesh.webapp.SiteMeshFilter
import com.typesafe.scalalogging.LazyLogging
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration
import org.springframework.boot.context.embedded.ServletContextInitializer
import org.springframework.context.annotation.{Import, Configuration}
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.filter.{DelegatingFilterProxy, HiddenHttpMethodFilter, CharacterEncodingFilter}
import org.springframework.web.servlet.DispatcherServlet
import slipp.config.WebMvcConfig

@Configuration
class SlippServletContextInitializer extends ServletContextInitializer with LazyLogging {
  override def onStartup(container: ServletContext): Unit = {
    val cef = new CharacterEncodingFilter
    cef.setEncoding("UTF-8")
    cef.setForceEncoding(true)
    container.addFilter("characterEncodingFilter", cef)
      .addMappingForUrlPatterns(null, false, "/*")

    container.addFilter("httpMethodFilter", classOf[HiddenHttpMethodFilter])
      .addMappingForUrlPatterns(EnumSet.allOf(classOf[DispatcherType]), false, "/*")

    container.addFilter("openEntityManagerInViewFilter", classOf[OpenEntityManagerInViewFilter])
      .addMappingForUrlPatterns(EnumSet.allOf(classOf[DispatcherType]), false, "/*")

    container.addFilter("springSecurityFilterChain", classOf[DelegatingFilterProxy])
      .addMappingForUrlPatterns(EnumSet.allOf(classOf[DispatcherType]), false, "/*")

    container.addFilter("sitemesh", classOf[SiteMeshFilter])
      .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), false, "/*")

    val webContext = new AnnotationConfigWebApplicationContext()
    webContext.register(classOf[WebMvcConfig])
    val dispatcher = container.addServlet("slipp", new DispatcherServlet(webContext))
    dispatcher.setLoadOnStartup(1)
    dispatcher.addMapping("/")
    logger.info("Initialized Servlet Context!")
  }
}
