package net.slipp.support.utils

import org.junit.Test
import org.slf4j.{Logger, LoggerFactory}

class MD5UtilTest {
  private val log: Logger = LoggerFactory.getLogger(classOf[MD5UtilTest])

  @Test def md5hex() {
    val email = "someone@somewhere.com"
    val hash = MD5Util.md5Hex(email)
    log.debug("hash : {}", hash)
  }
}
