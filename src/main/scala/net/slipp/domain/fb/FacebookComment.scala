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

case class FacebookComment(id: String, userId: String, name: String, createdTime: Date, message: String, groupId: String, groupName: String)
  extends Comparable[FacebookComment] {
  def getMessage = {
    br(links(message))
  }

  def compareTo(target: FacebookComment) = {
    (createdTime.getTime - target.createdTime.getTime).toInt
  }

  override def toString: String = {
    return "FacebookComment [id=" + id + ", userId=" + userId + ", name=" + name + ", createdTime=" + createdTime + ", message=" + message + "]"
  }
}
