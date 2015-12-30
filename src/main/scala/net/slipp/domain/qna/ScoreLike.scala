package net.slipp.domain.qna

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

object ScoreLike {
  def createLikedScoreLike(scoreLikeType: ScoreLikeType, socialUserId: Long, targetId: Long): ScoreLike = {
    new ScoreLike(scoreLikeType, socialUserId, targetId, true)
  }

  def createDisLikedScoreLike(scoreLikeType: ScoreLikeType, socialUserId: Long, targetId: Long): ScoreLike = {
    new ScoreLike(scoreLikeType, socialUserId, targetId, false)
  }
}

@Entity
@Table(name = "score_like")
class ScoreLike(likeType: ScoreLikeType, userId: Long, tId: Long, l: Boolean) {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private var id: Long = _

  @Enumerated(EnumType.STRING)
  @Column(name = "like_type", nullable = false, columnDefinition = "enum('ANSWER','QUESTION')")
  private var scoreLikeType: ScoreLikeType = likeType

  @Column(name = "social_user_id")
  private var socialUserId: Long = userId

  @Column(name = "target_id")
  private var targetId: Long = tId

  @Column(name = "liked")
  private var liked: Boolean = l

  def this() = this(ScoreLikeType.QUESTION, 0L, 0L, false)

  def getId: Long = {
    return id
  }

  def getSocialUserId: Long = {
    return socialUserId
  }

  def getScoreLikeType: ScoreLikeType = {
    return scoreLikeType
  }

  def getTargetId: Long = {
    return targetId
  }

  def isLiked: Boolean = {
    return liked
  }

  override def toString: String = {
    return "ScoreLike [id=" + id + ", scoreLikeType=" + scoreLikeType + ", socialUserId=" + socialUserId + ", targetId=" + targetId + ", liked=" + liked + "]"
  }
}
