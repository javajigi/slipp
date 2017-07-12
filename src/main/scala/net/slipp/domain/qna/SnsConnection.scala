package net.slipp.domain.qna

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

import net.slipp.domain.ProviderType
import net.slipp.domain.user.SocialUser
import net.slipp.support.jpa.DomainModel
import org.apache.commons.lang3.StringUtils

object SnsConnection {
  private[qna] def removeId(postId: String): String = {
    if (StringUtils.isBlank(postId)) {
      return postId
    }
    if (!postId.contains("_")) {
      return postId
    }
    val postIds: Array[String] = postId.split("_")
    if (postIds.length != 2) {
      return postId
    }
    return postIds(1)
  }
}

@Embeddable
class SnsConnection(sType: ProviderType, pId: String, gId: String) extends DomainModel {
  @Enumerated(EnumType.STRING)
  @Column(name = "sns_type", nullable = true, columnDefinition = "enum ('facebook', 'twitter', 'google', 'slipp')")
  private var snsType = sType

  @Column(name = "post_id", length = 100, nullable = true)
  private var postId = pId

  @Column(name = "group_id", length = 100, nullable = true)
  private var groupId = gId

  @Column(name = "sns_answer_count", nullable = false)
  private var snsAnswerCount = 0

  def this(snsType: ProviderType, postId: String) = this(snsType, postId, null)

  def this() = this(null, null, null)

  def isConnected: Boolean = {
    return !StringUtils.isBlank(postId)
  }

  def isGroupConnected: Boolean = {
    return !StringUtils.isBlank(groupId)
  }

  def getSnsType: ProviderType = {
    return snsType
  }

  def getPostId: String = {
    return postId
  }

  def getGroupId: String = {
    return groupId
  }

  def getSnsAnswerCount: Int = {
    return snsAnswerCount
  }

  def updateAnswerCount(answerCount: Int) {
    this.snsAnswerCount = answerCount
  }

  def migrationPostId(providerUserId: String) {
    if (isGroupConnected) {
      return;
    }

    if (postId.contains(providerUserId)) {
      return
    }

    this.postId = providerUserId + "_" + postId
  }
}
