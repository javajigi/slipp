package net.slipp.web.tag

import scala.beans.BeanProperty

import net.slipp.support.jpa.DomainModel

class TagForm(n: String) extends DomainModel {
  @BeanProperty
  var tagId: Long = _

  @BeanProperty
  var email: String = _

  @BeanProperty
  var name: String = n

  @BeanProperty
  var groupId: String = _

  @BeanProperty
  var description: String = _

  def this() = this(null)
}