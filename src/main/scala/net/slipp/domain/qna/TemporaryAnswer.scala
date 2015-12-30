package net.slipp.domain.qna

import java.lang.Long
import java.io.Serializable

import scala.beans.BeanProperty

private class EmptyTemporaryAnswer extends TemporaryAnswer {
  override def createAnswer: Answer = {
    return new Answer
  }

  override def generateUrl: String = {
    return "/"
  }
}

object TemporaryAnswer {
  val TEMPORARY_ANSWER_KEY: String = "temporaryAnswer"
  val EMPTY_ANSWER: TemporaryAnswer = new EmptyTemporaryAnswer
}

class TemporaryAnswer(qId: Long, tAnswer: String) extends Serializable {
  @BeanProperty
  val questionId: Long = qId

  @BeanProperty
  val temporaryAnswer: String = tAnswer

  def this() = this(null, null)

  def createAnswer = new Answer(this.temporaryAnswer)

  def generateUrl: String = {
    return String.format("/questions/%d", this.questionId)
  }

  override def toString: String = {
    return "TemporaryAnswer [questionId=" + questionId + ", temporaryAnswer=" + temporaryAnswer + "]"
  }
}
