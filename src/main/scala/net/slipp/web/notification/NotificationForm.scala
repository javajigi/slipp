package net.slipp.web.notification

import net.slipp.support.jpa.DomainModel
import scala.beans.BeanProperty

class NotificationForm(qId: Long, t: String, r: Boolean) extends DomainModel {
  @BeanProperty
  var questionId= qId
  
  @BeanProperty
  var title = t
  
  @BeanProperty
  var readed = r
}