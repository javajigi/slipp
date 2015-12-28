package net.slipp.social.security

import org.junit.Assert._
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.encoding.ShaPasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class Sha256ToBcryptPasswordEncoderTest {
  private val logger: Logger = LoggerFactory.getLogger(classOf[Sha256ToBcryptPasswordEncoderTest])
  private var sha256ToBCryptPasswordEncoder: Sha256ToBCryptPasswordEncoder = null

  @Before def setup {
    sha256ToBCryptPasswordEncoder = new Sha256ToBCryptPasswordEncoder
    sha256ToBCryptPasswordEncoder.setBcryptPasswordEncoder(new BCryptPasswordEncoder)
    sha256ToBCryptPasswordEncoder.setSha256PasswordEncoder(new ShaPasswordEncoder(256))
  }

  @Test def shaPasswordEncoder {
    val passwordEncoder: ShaPasswordEncoder = new ShaPasswordEncoder(256)
    val result: String = passwordEncoder.encodePassword("password", "")
    logger.debug("encoded password : {}, length : {}", result, result.length)
    assertTrue(sha256ToBCryptPasswordEncoder.matches("password", result))
  }

  @Test def bcryptPasswordEncoder {
    val passwordEncoder: BCryptPasswordEncoder = new BCryptPasswordEncoder
    val result: String = passwordEncoder.encode("password")
    logger.debug("encoded password : {}, length : {}", result, result.length)
    assertTrue(sha256ToBCryptPasswordEncoder.matches("password", result))
  }
}
