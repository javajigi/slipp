package net.slipp.domain.user

import java.lang.Long
import scala.beans.BeanProperty

class PasswordDto(i: Long, oPass: String, nPass: String, nPasswordConfirm: String) {
  @BeanProperty
  var id: Long = i

  @BeanProperty
  var oldPassword: String = oPass

  @BeanProperty
  var newPassword: String = nPass

  @BeanProperty
  var newPasswordConfirm: String = nPasswordConfirm

  def this() = this(null, null, null, null)

  def this(id: Long) = this(id, null, null, null)
}
