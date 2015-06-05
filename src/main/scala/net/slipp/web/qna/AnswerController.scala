package net.slipp.web.qna

import javax.annotation.Resource
import javax.servlet.http.HttpSession
import net.slipp.domain.qna.Answer
import net.slipp.domain.qna.TemporaryAnswer
import net.slipp.domain.user.SocialUser
import net.slipp.service.qna.QnaService
import net.slipp.service.tag.TagService
import net.slipp.support.web.argumentresolver.LoginUser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import com.typesafe.scalalogging.LazyLogging

@Controller
@RequestMapping(Array("/questions/{questionId}/answers"))
class AnswerController(
  @Resource(name = "qnaService") qnaService: QnaService,
  @Resource(name = "tagService") tagService: TagService) extends LazyLogging {

  @RequestMapping(value = Array(""), method = Array(RequestMethod.POST))
  def create(@LoginUser loginUser: SocialUser, @PathVariable questionId: Long, answer: Answer, session: HttpSession) = {
    logger.debug(s"questionId : $questionId, answer : $answer")
    qnaService.createAnswer(loginUser, questionId, answer)
    session.removeAttribute(TemporaryAnswer.TEMPORARY_ANSWER_KEY)
    "redirect:/questions/%d".format(questionId)
  }

  @RequestMapping(value = Array("{answerId}"), method = Array(RequestMethod.DELETE))
  def delete(@LoginUser loginUser: SocialUser, @PathVariable questionId: Long, @PathVariable answerId: Long) = {
    qnaService.deleteAnswer(loginUser, questionId, answerId)
    "redirect:/questions/%d".format(questionId)
  }

  @RequestMapping(Array("{answerId}/form"))
  def updateForm(@LoginUser loginUser: SocialUser, @PathVariable questionId: Long, @PathVariable answerId: Long, model: Model) = {
    val answer = qnaService.findAnswerById(answerId)
    if (!answer.isWritedBy(loginUser)) {
      throw new AccessDeniedException(loginUser.getUserId() + " is not owner!")
    }

    model.addAttribute("question", qnaService.findByQuestionId(questionId))
    model.addAttribute("answer", answer)
    model.addAttribute("tags", tagService.findLatestTags())
    "qna/answer"
  }
  
  @RequestMapping(value = Array("{answerId}"), method = Array(RequestMethod.PUT))
  def update(@LoginUser loginUser: SocialUser, @PathVariable questionId: Long, @PathVariable answerId: Long, answer: Answer) = {
    qnaService.updateAnswer(loginUser, answer)
    "redirect:/questions/%d#answer-%d".format(questionId, answerId)
  }

  def this() = this(null, null)
}