package net.slipp.domain.tag

import javax.persistence.Column
import javax.persistence.Embeddable
import org.apache.commons.lang3.StringUtils

object TagInfo {
  val FACEBOOK_GROUP_URL_PREFIX: String = "https://www.facebook.com/groups/"
}

@Embeddable
class TagInfo(gId: String, d: String)  {
  @Column(length = 100, nullable = true)
  private var groupId: String = gId

  @Column(length = 1000, nullable = true)
  private var description: String = d

  def this() = this(null, null)

  def getGroupId: String = {
    return groupId
  }

  def getDescription: String = {
    return description
  }

  def isConnectGroup: Boolean = {
    return !StringUtils.isBlank(groupId)
  }

  def getGroupUrl: String = {
    if (isConnectGroup) {
      return TagInfo.FACEBOOK_GROUP_URL_PREFIX + groupId
    }
    return ""
  }

  override def toString: String = {
    return "TagInfo [groupId=" + groupId + ", description=" + description + "]"
  }
}
