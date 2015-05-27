package net.slipp.web

import scala.beans.BeanProperty
import org.apache.commons.lang3.StringUtils
import java.util.regex.Pattern

class UserForm(uId: String, e: String) {
  @BeanProperty
  var userId = uId

  @BeanProperty
  var email = e
  
  def isValid(): Boolean = {
    if (StringUtils.isBlank(userId) || StringUtils.isBlank(email)) {
      return false
    }

    var m = Pattern.compile("(.){2,12}").matcher(userId)
    if (!m.matches()) {
      return false
    }

    m = Pattern.compile("[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})").matcher(email)
    if (!m.matches()) {
      return false
    }
    
    return true
  }

  def this() = this(null, null)
}