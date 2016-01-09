package net.slipp.support.test

import com.google.common.collect.Sets
import net.slipp.domain.qna.{ScoreLikeType, ScoreLike, Answer, Question}
import net.slipp.domain.tag.Tag
import net.slipp.domain.user.SocialUser

trait Fixture {
  implicit def aSomeUser(id: Long = 1L, userId: String = "someUserId", email: String = "some@sample.com",
                         password: String = "password", rawPassword: String = "rawPassword") = {
    val user = new SocialUser()
    user.setId(id)
    user.setUserId(userId)
    user.setEmail(email)
    user.setPassword(password)
    user.setRawPassword(rawPassword)
    user
  }

  implicit def aSomeQuestion(loginUser: SocialUser = aSomeUser(), title: String = "title",
                             contents: String = "contents", tags: Set[Tag] = Set.empty) = {
    val q = new Question(loginUser, title, contents, null)
    tags.foreach(t => q.taggedTag(t))
    q
  }

  implicit def aSomeAnswer(id: Long = 1L, q: Question = aSomeQuestion(), sumLiked: Integer = 1) = {
    val answer = new Answer
    answer.setAnswerId(id)
    answer.setQuestion(q)
    (1 to sumLiked).foreach(_ => answer.upRank())
    answer
  }

  implicit def aSomeTag(id: Long = 0L, name: String = "tag", taggedCount: Int = 0) = {
    val tag = Tag.pooledTag(name)
    tag.setTagId(id)
    tag.setTaggedCount(taggedCount)
    tag
  }

  def aSomeQuestionScoreLike(socialUserId: Long = 1L, targetId: Long = 1L) = {
    aSomeScore(ScoreLikeType.QUESTION, socialUserId, targetId, true)
  }

  def aSomeAnswerScoreLike(socialUserId: Long = 1L, targetId: Long = 1L) = {
    aSomeScore(ScoreLikeType.ANSWER, socialUserId, targetId, true)
  }

  def aSomeAnswerScoreDisLike(socialUserId: Long = 1L, targetId: Long = 1L) = {
    aSomeScore(ScoreLikeType.ANSWER, socialUserId, targetId, false)
  }

  private def aSomeScore(scoreLikeType: ScoreLikeType,
                              socialUserId: Long,
                              targetId: Long,
                              liked: Boolean) = {
    if (liked) {
      ScoreLike.createLikedScoreLike(scoreLikeType, socialUserId, targetId)
    } else {
      ScoreLike.createDisLikedScoreLike(scoreLikeType, socialUserId, targetId)
    }
  }
}
