package net.slipp.support.utils

import org.junit.Test
import org.pegdown.PegDownProcessor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SlippStringUtilsTest {
  private final val logger: Logger = LoggerFactory.getLogger(classOf[SlippStringUtilsTest])

  @Test def convertMarkdownLinks {
    val contents: String = "Please go to http://stackoverflow.com and then mailto:oscarreyes@wordpress.com to download a file from    ftp://user:pass@someserver/someFile.txt"
    var result: String = SlippStringUtils.convertMarkdownLinks(contents)
    logger.debug("result : {}", result)
    result = new PegDownProcessor().markdownToHtml(result)
    logger.debug("result : {}", result)
  }
}