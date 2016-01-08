package net.slipp.domain.fb

import java.util.Date

import com.restfb.types.{CategorizedFacebookType, Comment}
import net.slipp.domain.tag.Tag
import net.slipp.support.web.tags.SlippFunctions.{br, links}

import scala.beans.BeanProperty

object FacebookComment {
  def create(tag: Tag, comment: Comment): FacebookComment = {
    val user: CategorizedFacebookType = comment.getFrom
    if (tag == null) {
      return new FacebookComment(comment.getId, user.getId, user.getName, comment.getCreatedTime, comment.getMessage, null, null)
    }
    return new FacebookComment(comment.getId, user.getId, user.getName, comment.getCreatedTime, comment.getMessage, tag.getGroupId, tag.getName)
  }
}

class FacebookComment(i: String, uid: String, n: String, cTime: Date, m: String, gId: String, gName: String)
  extends Comparable[FacebookComment] {
  @BeanProperty
  val id = i

  @BeanProperty
  val userId = uid

  @BeanProperty
  val name = n

  @BeanProperty
  val createdTime = cTime

  val message = m

  @BeanProperty
  val groupId = gId

  @BeanProperty
  val groupName = gName

  def getMessage = {
    br(links(message))
  }

  def compareTo(target: FacebookComment) = {
    (createdTime.getTime - target.getCreatedTime.getTime).toInt
  }

  override def toString: String = {
    return "FacebookComment [id=" + id + ", userId=" + userId + ", name=" + name + ", createdTime=" + createdTime + ", message=" + message + "]"
  }
}
