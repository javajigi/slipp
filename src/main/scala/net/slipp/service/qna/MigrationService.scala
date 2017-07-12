package net.slipp.service.qna

import java.util.List
import javax.annotation.Resource

import net.slipp.domain.qna.Question
import net.slipp.repository.qna.{AnswerRepository, QuestionRepository}
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConversions._

@Service
@Transactional
class MigrationService {
  @Resource(name = "questionRepository") private var questionRepository: QuestionRepository = _
  @Resource(name = "answerRepository") private var answerRepository: AnswerRepository = _

  def convertConfluenceToMarkdown {
    val questions: List[Question] = questionRepository.findAll
    questions.foreach(q => q.convertWiki())
    answerRepository.findAll.foreach(a => a.convertWiki())
  }

  def migrateFacebookPostId {
    val questions = questionRepository.findByDeleted(false);
    questions.foreach(question => {
      question.migrateFacebookPostId
    });
  }
}