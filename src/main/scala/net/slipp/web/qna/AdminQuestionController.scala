package net.slipp.web.qna

import javax.annotation.Resource
import javax.servlet.http.HttpSession
import net.slipp.domain.qna.Question
import net.slipp.domain.qna.QuestionDto
import net.slipp.service.qna.QnaService
import net.slipp.service.tag.TagService
import net.slipp.web.UserForm
import net.slipp.web.QnAPageableHelper._
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import com.typesafe.scalalogging.LazyLogging

@Controller
@RequestMapping(Array("/admin/questions"))
class AdminQuestionController(
  @Resource(name = "qnaService") qnaService: QnaService,
  @Resource(name = "tagService") tagService: TagService) extends LazyLogging {
  private val DefaultPageSize = 30

  @RequestMapping(Array(""))
  def index(page: Integer, searchTerm: String, model: Model) = {
    logger.debug("search keyword : {}", searchTerm)
    model.addAttribute("questions", qnaService.findsAllQuestion(searchTerm, createPageableByQuestionUpdatedDate(page, DefaultPageSize)))
    model.addAttribute("searchTerm", searchTerm)
    "admin/qna/list"
  }

  @RequestMapping(Array("/{id}"))
  def show(@PathVariable id: Long, searchTerm: String, model: Model) = {
    model.addAttribute("question", qnaService.showQuestion(id))
    model.addAttribute("tags", tagService.findLatestTags)
    model.addAttribute("user", new UserForm())
    model.addAttribute("searchTerm", searchTerm)
    "admin/qna/show"
  }

  @RequestMapping(value = Array("/{id}"), method = Array(RequestMethod.DELETE))
  def delete(@PathVariable id: Long, searchTerm: String) = {
    val question = qnaService.findByQuestionId(id)
    qnaService.deleteQuestion(question.getWriter, id)
    "redirect:/admin/questions?searchTerm=%s".format(searchTerm)
  }

  @RequestMapping(Array("/{id}/form"))
  def updateForm(@PathVariable id: Long, searchTerm: String, model: Model) = {
    model.addAttribute("question", qnaService.findByQuestionId(id))
    model.addAttribute("searchTerm", searchTerm)
    "admin/qna/form"
  }

  @RequestMapping(value = Array("/{id}"), method = Array(RequestMethod.PUT))
  def update(@PathVariable id: Long, searchTerm: String, updatedQuestion: QuestionDto) = {
    logger.debug("Question : {}", updatedQuestion)
    val originalQuestion = qnaService.findByQuestionId(id)
    qnaService.updateQuestionByAdmin(originalQuestion.getWriter, updatedQuestion)
    "redirect:/admin/questions/%d?searchTerm=%s".format(id, searchTerm)
  }
  
  def this() = this(null, null)
}