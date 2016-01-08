package net.slipp.social.security

import scala.beans.BeanProperty

class SignUpForm(uId: String) {
  @BeanProperty
  var userId = uId

  override def toString: String = {
    return "SignUpForm [userId=" + userId + "]"
  }

  def this() = this(null)
}
