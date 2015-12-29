package net.slipp.service.user

import org.apache.commons.lang3.RandomStringUtils

trait PasswordGenerator {
  def generate: String
}

object PasswordGenerator {
  val DEFAULT_RANDOM_PASSWORD_LENGTH: Int = 12
  val DEFAULT_FIXED_PASSWORD: String = "password"
}

class FixedPasswordGenerator(p: String) extends PasswordGenerator {
  private val password = p

  def generate: String = {
    return password
  }

  def this() = this(PasswordGenerator.DEFAULT_FIXED_PASSWORD)
}

class RandomPasswordGenerator extends PasswordGenerator {
  def generate: String = {
    return RandomStringUtils.randomAlphanumeric(PasswordGenerator.DEFAULT_RANDOM_PASSWORD_LENGTH)
  }
}
