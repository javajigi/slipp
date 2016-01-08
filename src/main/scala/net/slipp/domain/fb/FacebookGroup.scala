package net.slipp.domain.fb

import com.restfb.Facebook

class EmptyFacebookGroup extends FacebookGroup(null, null) {
  override def isEmpty = {
    true
  }
}

class FacebookGroup(gId: String, n: String) {
  @Facebook("gid") private val groupId = gId
  @Facebook private val name = n

  def getGroupId = {
    groupId
  }

  def getName = {
    name
  }

  def isEmpty = {
    false
  }

  override def toString: String = {
    return "FacebookGroup [groupId=" + groupId + ", name=" + name + "]"
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[FacebookGroup]

  override def equals(other: Any): Boolean = other match {
    case that: FacebookGroup =>
      (that canEqual this) &&
        groupId == that.groupId &&
        name == that.name
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(groupId, name)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
