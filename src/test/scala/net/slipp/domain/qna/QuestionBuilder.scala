package net.slipp.domain.qna

import java.lang.Long
import java.util.List

import com.google.common.collect.{Lists, Sets}
import net.slipp.domain.tag.Tag
import net.slipp.domain.user.SocialUser

object QuestionBuilder {
  def aQuestion: QuestionBuilder = {
    return new QuestionBuilder(null)
  }

  def aQuestion(questionId: Long): QuestionBuilder = {
    return new QuestionBuilder(questionId)
  }
}

class QuestionBuilder(questionId: Long) {
  private var writer: SocialUser = null
  private var title: String = null
  private var contents: String = null
  private var tags = Sets.newHashSet[Tag]
  private var answers = Lists.newArrayList[Answer]

  def withWriter(writer: SocialUser): QuestionBuilder = {
    this.writer = writer
    return this
  }

  def withTitle(title: String): QuestionBuilder = {
    this.title = title
    return this
  }

  def withContents(contents: String): QuestionBuilder = {
    this.contents = contents
    return this
  }

  def withTag(tag: Tag): QuestionBuilder = {
    tags.add(tag)
    return this
  }

  def withAnswer(answer: Answer): QuestionBuilder = {
    answers.add(answer)
    return this
  }

  def build: Question = {
    val question: Question = new Question(questionId, writer, title, contents, tags) {
      override def getAnswers: List[Answer] = {
        return answers
      }
    }
    import scala.collection.JavaConversions._
    for (answer <- answers) {
      question.newAnswered(answer)
    }
    return question
  }
}
