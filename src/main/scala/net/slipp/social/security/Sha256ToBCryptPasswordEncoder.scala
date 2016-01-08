package net.slipp.social.security

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

class Sha256ToBCryptPasswordEncoder extends PasswordEncoder {
  private var bcryptPasswordEncoder: PasswordEncoder = null
  private var sha256PasswordEncoder: MessageDigestPasswordEncoder = null

  def encode(rawPassword: CharSequence): String = {
    return bcryptPasswordEncoder.encode(rawPassword)
  }

  def matches(rawPassword: CharSequence, encodedPassword: String): Boolean = {
    if (encodedPassword.startsWith("$2a$10$") && encodedPassword.length == 60) {
      return bcryptPasswordEncoder.matches(rawPassword, encodedPassword)
    }
    if (encodedPassword.length == 64) {
      return sha256PasswordEncoder.isPasswordValid(encodedPassword, rawPassword.toString, null)
    }
    return false
  }

  def setBcryptPasswordEncoder(bcryptPasswordEncoder: PasswordEncoder) {
    this.bcryptPasswordEncoder = bcryptPasswordEncoder
  }

  def setSha256PasswordEncoder(sha256PasswordEncoder: MessageDigestPasswordEncoder) {
    this.sha256PasswordEncoder = sha256PasswordEncoder
  }
}
