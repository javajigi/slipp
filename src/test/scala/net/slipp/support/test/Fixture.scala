package net.slipp.support.test

import net.slipp.domain.qna.{ScoreLikeType, ScoreLike, Answer, Question}
import net.slipp.domain.user.SocialUser

trait Fixture {
  implicit def aSomeUser(id: Long = 1L, userId: String = "someUserId", email: String = "some@sample.com") = {
    val user = new SocialUser()
    user.setId(id)
    user.setUserId(userId)
    user.setEmail(email)
    user
  }

  implicit def aSomeQuestion(loginUser: SocialUser = aSomeUser(), title: String = "title", contents: String = "contents") = {
    new Question(loginUser, title, contents, null)
  }

  implicit def aSomeAnswer(id: Long = 1L, q: Question = aSomeQuestion()) = {
    val answer = new Answer
    answer.setAnswerId(id)
    answer.setQuestion(q)
    answer
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
