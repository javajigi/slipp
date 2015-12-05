package net.slipp.support.web

import java.util.EnumSet
import javax.servlet.{DispatcherType, ServletContext}

import com.opensymphony.sitemesh.webapp.SiteMeshFilter
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter
import org.springframework.web.WebApplicationInitializer
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.support.{XmlWebApplicationContext, AnnotationConfigWebApplicationContext}
import org.springframework.web.filter.{CharacterEncodingFilter, DelegatingFilterProxy, HiddenHttpMethodFilter}
import org.springframework.web.servlet.DispatcherServlet
import slipp.config.{WebMvcConfig, ApplicationConfig}

class SlippWebInit extends WebApplicationInitializer {
  override def onStartup(container: ServletContext): Unit = {
    val appContext = new AnnotationConfigWebApplicationContext()
    appContext.register(classOf[ApplicationConfig])
    container.addListener(new ContextLoaderListener(appContext))

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
    webContext.setParent(appContext)
    webContext.register(classOf[WebMvcConfig])
    val dispatcher = container.addServlet("slipp", new DispatcherServlet(webContext))
    dispatcher.setLoadOnStartup(1)
    dispatcher.addMapping("/")
  }
}
