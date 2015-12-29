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

object CssPreProcessorInterceptor {
  private val DEFAULT_WINDOWS_NAME: String = "Windows"
}

class CssPreProcessorInterceptor extends HandlerInterceptorAdapter {
  private val log: Logger = LoggerFactory.getLogger(classOf[GlobalRequestAttributesInterceptor])

  @Autowired private var env: Environment = null

  @PostConstruct def afterPropertiesSet {
    val environment: String = env.getProperty("environment")
    log.info("current environment : {}", environment)
    Assert.notNull(environment, "Environment cann't be null!")
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
      val file: File = new File("./webapp/WEB-INF/")
      val pb: ProcessBuilder = new ProcessBuilder(file.getAbsolutePath + "/" + shellFileName)
      log.debug("stylus file path : {}", file.getAbsolutePath)
      pb.directory(new File(file.getAbsolutePath))
      val process: Process = pb.start
      val writer: StringWriter = new StringWriter
      IOUtils.copy(process.getInputStream, writer, "UTF-8")
      log.debug(writer.toString)
    }
    catch {
      case e: Exception => {
        log.warn("cannot find styl.sh file!")
      }
    }
  }

  private def shellFileName: String = {
    if (isWindows) {
      return "styl.bat"
    }
    return "styl.sh"
  }

  private def isWindows: Boolean = {
    val osName: String = System.getProperty("os.name")
    return osName.contains(CssPreProcessorInterceptor.DEFAULT_WINDOWS_NAME)
  }
}
