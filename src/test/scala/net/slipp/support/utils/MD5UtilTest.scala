package net.slipp.support.utils

import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MD5UtilTest {
  private val log: Logger = LoggerFactory.getLogger(classOf[MD5UtilTest])

  @Test def md5hex {
    val email: String = "someone@somewhere.com"
    val hash: String = MD5Util.md5Hex(email)
    log.debug("hash : {}", hash)
  }
}
