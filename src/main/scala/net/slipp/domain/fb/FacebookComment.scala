package net.slipp.domain.fb

import java.util.Date

import com.restfb.types.{CategorizedFacebookType, Comment}
import net.slipp.domain.tag.Tag
import net.slipp.support.web.tags.SlippFunctions.{br, links}

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
  private val id = i
  private val userId = uid
  private val name = n
  private val createdTime = cTime
  private val message = m
  private val groupId = gId
  private val groupName = gName

  def getId = {
    id
  }

  def getCreatedTime = {
    createdTime
  }

  def getMessage = {
    br(links(message))
  }

  def getUserId = {
    userId
  }

  def getName = {
    name
  }

  def getGroupId = {
    groupId
  }

  def getGroupName = {
    groupName
  }

  def compareTo(target: FacebookComment) = {
    (createdTime.getTime - target.getCreatedTime.getTime).toInt
  }

  override def toString: String = {
    return "FacebookComment [id=" + id + ", userId=" + userId + ", name=" + name + ", createdTime=" + createdTime + ", message=" + message + "]"
  }
}
