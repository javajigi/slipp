package net.slipp.service.qna

import java.util.List
import javax.annotation.Resource

import net.slipp.domain.qna.{Answer, Question}
import net.slipp.repository.qna.{AnswerRepository, QuestionRepository}
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import scala.collection.JavaConversions._

@Service
@Transactional
class MigrationService {
  @Resource(name = "questionRepository") private var questionRepository: QuestionRepository = null
  @Resource(name = "answerRepository") private var answerRepository: AnswerRepository = null

  def convertConfluenceToMarkdown {
    val questions: List[Question] = questionRepository.findAll
    for (question <- questions) {
      question.convertWiki
    }
    val answers: List[Answer] = answerRepository.findAll
    for (answer <- answers) {
      answer.convertWiki
    }
  }
}