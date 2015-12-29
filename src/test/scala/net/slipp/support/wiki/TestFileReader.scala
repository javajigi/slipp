package net.slipp.support.wiki

import java.io.IOException
import org.apache.commons.io.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object TestFileReader {
  private val logger: Logger = LoggerFactory.getLogger(classOf[TestFileReader])

  def read(target: AnyRef, fileName: String): String = {
    try {
      val location: String = target.getClass.getPackage.getName.replaceAll("\\.", "/") + "/" + fileName
      logger.debug("location : {}", location)
      return IOUtils.toString(target.getClass.getClassLoader.getResourceAsStream(location), "UTF-8")
    }
    catch {
      case e: IOException => {
        throw new RuntimeException("Cannot load ", e)
      }
    }
  }
}

class TestFileReader

