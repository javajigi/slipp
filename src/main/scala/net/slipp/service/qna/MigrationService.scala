package net.slipp.service.qna

import javax.annotation.Resource

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
    questionRepository.findAll.foreach(q => q.convertWiki())
    answerRepository.findAll.foreach(a => a.convertWiki())
  }
}