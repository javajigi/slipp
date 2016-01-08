package net.slipp.web.qna

import javax.annotation.Resource
import javax.servlet.http.HttpSession
import net.slipp.domain.qna.Question
import net.slipp.domain.qna.QuestionDto
import net.slipp.domain.qna.TemporaryAnswer
import net.slipp.domain.user.SocialUser
import net.slipp.service.qna.QnaService
import net.slipp.service.tag.TagService
import net.slipp.support.web.argumentresolver.LoginUser
import net.slipp.web.UserForm
import net.slipp.web.QnAPageableHelper._

import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import com.restfb.util.StringUtils

import com.typesafe.scalalogging.LazyLogging

@Controller
@RequestMapping(Array("/questions"))
class QuestionController(
  @Resource(name = "qnaService") qnaService: QnaService,
  @Resource(name = "tagService") tagService: TagService) extends LazyLogging {
  private val DefaultPageSize = 15

  @RequestMapping(Array(""))
  def index(page: Integer, model: Model) = {
    logger.debug("currentPage : {}", page)
    model.addAttribute("questions", qnaService.findsQuestion(createPageableByQuestionUpdatedDate(page, DefaultPageSize)))
    model.addAttribute("tags", tagService.findLatestTags)
    "qna/list"
  }

  @RequestMapping(Array("/form"))
  def createForm(@LoginUser loginUser: SocialUser, currentTag: String, model: Model) = {
    logger.debug("currentTag : {}", currentTag)
    model.addAttribute("question", new QuestionDto())
    "qna/form"
  }

  @RequestMapping(value = Array(""), method = Array(RequestMethod.POST))
  def create(@LoginUser loginUser: SocialUser, newQuestion: QuestionDto) = {
    logger.debug("Question : {}", newQuestion)
    val question = qnaService.createQuestion(loginUser, newQuestion)
    "redirect:/questions/%d".format(question.getQuestionId)
  }

  @RequestMapping(Array("/{id}/form"))
  def updateForm(@LoginUser loginUser: SocialUser, @PathVariable id: Long, model: Model) = {
    val question = qnaService.findByQuestionId(id)
    if (!question.isWritedBy(loginUser)) {
      throw new AccessDeniedException(loginUser.getUserId + " is not owner!")
    }
    model.addAttribute("question", question)
    "qna/form"
  }

  @RequestMapping(value = Array(""), method = Array(RequestMethod.PUT))
  def update(@LoginUser loginUser: SocialUser, updatedQuestion: QuestionDto) = {
    logger.debug("Question : {}", updatedQuestion)
    val question = qnaService.updateQuestion(loginUser, updatedQuestion)
    "redirect:/questions/%d".format(question.getQuestionId)
  }

  @RequestMapping(Array("/{id}"))
  def show(@PathVariable id: Long, model: Model, session: HttpSession) = {
    val question = qnaService.showQuestion(id)
    if (question.isDeleted) {
      throw new AccessDeniedException(id + " question is deleted.")
    }

    model.addAttribute("answer", getTemporaryAnswer(session).createAnswer)
    model.addAttribute("question", question)
    model.addAttribute("tags", tagService.findLatestTags)
    model.addAttribute("user", new UserForm())
    "qna/show"
  }

  private def getTemporaryAnswer(session: HttpSession) = {
    val value = session.getAttribute(TemporaryAnswer.TEMPORARY_ANSWER_KEY)
    if (value == null) TemporaryAnswer.EMPTY_ANSWER else value.asInstanceOf[TemporaryAnswer]
  }

  @RequestMapping(value = Array("/{id}"), method = Array(RequestMethod.DELETE))
  def delete(@LoginUser loginUser: SocialUser, @PathVariable id: Long) = {
    qnaService.deleteQuestion(loginUser, id)
    "redirect:/questions"
  }

  @RequestMapping(value = Array("/{id}/tagged"), method = Array(RequestMethod.POST))
  def tagged(@LoginUser loginUser: SocialUser, @PathVariable id: Long, taggedName: String) = {
    logger.debug("tagged name : {}", taggedName)

    if (StringUtils.isBlank(taggedName)) {
      throw new NullPointerException("Tag name should not null.")
    }
    qnaService.tagged(loginUser, id, taggedName)
    "redirect:/questions/%d".format(id)
  }

  @RequestMapping(Array("/tagged/{name:.+}"))
  def listByTagged(@PathVariable name: String, page: Integer, model: Model) = {
    logger.debug("tagged name : {}", name)

    model.addAttribute("currentTag", tagService.findTagByName(name))
    model.addAttribute("questions", qnaService.findsByTag(name, createPageableByQuestionUpdatedDate(page, DefaultPageSize)))
    model.addAttribute("tags", tagService.findLatestTags)
    "qna/list"
  }
  
  @RequestMapping(value = Array("/{id}/connect/facebook"), method = Array(RequestMethod.POST))
  def connectFB(@PathVariable id: Long, fbPostId: String) = {
    qnaService.connectFB(id, fbPostId)
    "redirect:/questions/%d".format(id)
  }

  def this() = this(null, null)
}