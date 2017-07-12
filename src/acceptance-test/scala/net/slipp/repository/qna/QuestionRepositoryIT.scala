package net.slipp.repository.qna

import net.slipp.domain.qna.Question
import org.junit.Test
import org.junit.runner.RunWith
import org.pegdown.{Extensions, PegDownProcessor}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort.Direction
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional
import slipp.config.PersistenceJPAConfig

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[PersistenceJPAConfig]))
@Transactional
class QuestionRepositoryIT {
  private val logger: Logger = LoggerFactory.getLogger(classOf[QuestionRepositoryIT])
  @Autowired private var dut: QuestionRepository = null

  @Test def findsByTag {
    val page: PageRequest = new PageRequest(0, 5, Direction.DESC, "createdDate")
    val name: String = "eclipse"
    dut.findsByTag(name, page)
  }

  @Test
  def findById {
    val question: Question = dut.findOne(270L)
    logger.debug("contents : {}", question.getContents)
    val html: String = new PegDownProcessor(Extensions.FENCED_CODE_BLOCKS).markdownToHtml(question.getContents)
    logger.debug("html : {}", html)
  }
}
