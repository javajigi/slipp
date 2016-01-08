package net.slipp.web.qna

import javax.annotation.Resource
import javax.servlet.http.HttpSession
import net.slipp.domain.qna.{QuestionDto, Answer, TemporaryAnswer}
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
      throw new AccessDeniedException(loginUser.getUserId + " is not owner!")
    }

    model.addAttribute("question", qnaService.findByQuestionId(questionId))
    model.addAttribute("answer", answer)
    model.addAttribute("tags", tagService.findLatestTags)
    "qna/answer"
  }
  
  @RequestMapping(value = Array("{answerId}"), method = Array(RequestMethod.PUT))
  def update(@LoginUser loginUser: SocialUser, @PathVariable questionId: Long, @PathVariable answerId: Long, answer: Answer) = {
    qnaService.updateAnswer(loginUser, answer)
    "redirect:/questions/%d#answer-%d".format(questionId, answerId)
  }

  @RequestMapping(value = Array("{answerId}/to"), method = Array(RequestMethod.GET))
  def newQuestionForm(@PathVariable questionId: Long, @PathVariable answerId: Long, model: Model) = {
    val question = qnaService.findByQuestionId(questionId)
    val answer = qnaService.findAnswerById(answerId)

    val questionDto = new QuestionDto(questionId, answerId, answer.getContents)
    val answers = question.getAnswers
    answers.remove(answer)

    model.addAttribute("question", questionDto)
    model.addAttribute("answers", answers)
    "qna/newquestion"
  }

  @RequestMapping(value = Array("{answerId}/to"), method = Array(RequestMethod.POST))
  def newQuestion(@LoginUser loginUser: SocialUser, @PathVariable questionId: Long, @PathVariable answerId: Long, newQuestion: QuestionDto) = {
    logger.debug("New Question : {}", newQuestion)
    val question = qnaService.moveQuestion(loginUser, questionId, newQuestion)
    "redirect:/questions/%d".format(question.getQuestionId)
  }

  def this() = this(null, null)
}