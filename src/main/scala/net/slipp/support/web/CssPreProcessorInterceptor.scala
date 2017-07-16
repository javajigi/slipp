package net.slipp.support.web

import java.io.{File, StringWriter}
import javax.annotation.PostConstruct
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.apache.commons.io.IOUtils
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.util.Assert
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

class CssPreProcessorInterceptor extends HandlerInterceptorAdapter {
  private val log: Logger = LoggerFactory.getLogger(classOf[GlobalRequestAttributesInterceptor])

  @Autowired private var env: Environment = null

  @PostConstruct def afterPropertiesSet {
    val environment: String = env.getProperty("environment")
    log.info("current environment : {}", environment)
    Assert.notNull(environment, "Environment can not be null!")
  }

  @throws(classOf[Exception])
  override def preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: AnyRef): Boolean = {
    if (isDevelopEnvironment) {
      runStylus
    }
    return super.preHandle(request, response, handler)
  }

  private def isDevelopEnvironment: Boolean = {
    return "DEVELOPMENT" == env.getProperty("environment")
  }

  private def runStylus {
    try {
      val pb: ProcessBuilder = new ProcessBuilder("npm run styl")
      val process: Process = pb.start
      val writer: StringWriter = new StringWriter
      IOUtils.copy(process.getInputStream, writer, "UTF-8")
      log.debug(writer.toString)
    }
    catch {
      case e: Exception => {
        log.warn("Fail to running npm task! please check NodeJS and run `npm install`, `npm run styl` manually.")
      }
    }
  }
}
