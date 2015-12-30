package net.slipp.support.security

import org.springframework.core.ErrorCoded

class LoginRequiredException extends Exception with ErrorCoded {
  private val ERROR_CODE: String = "exception.login.required"

  def getErrorCode = ERROR_CODE
}
