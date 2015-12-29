package net.slipp.support.utils

import org.apache.commons.lang3.RandomStringUtils
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RandomStringUtilsTest {
  private val log: Logger = LoggerFactory.getLogger(classOf[RandomStringUtilsTest])

  @Test
  def generateRandomPassword {
    log.debug("password : {}", RandomStringUtils.randomAlphanumeric(12))
  }
}
