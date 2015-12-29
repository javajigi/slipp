package net.slipp.domain.qna

import java.lang.Long
import net.slipp.domain.user.SocialUser

object AnswerBuilder {
  def anAnswer: AnswerBuilder = {
    return new AnswerBuilder(null)
  }

  def anAnswer(answerId: Long): AnswerBuilder = {
    return new AnswerBuilder(answerId)
  }
}

class AnswerBuilder(answerId: Long){
  private var totalLiked: Int = 0
  private var writer: SocialUser = null

  def withTotalLiked(totalLiked: Int): AnswerBuilder = {
    this.totalLiked = totalLiked
    return this
  }

  def `with`(writer: SocialUser): AnswerBuilder = {
    this.writer = writer
    return this
  }

  def build: Answer = {
    val answer: Answer = new Answer() {
      override def getSumLike: Integer = {
        return totalLiked
      }
    }
    answer.setAnswerId(answerId)
    answer.writedBy(writer)
    return answer
  }
}
