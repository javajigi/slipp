package net.slipp.web.qna

import javax.annotation.Resource
import javax.servlet.http.HttpSession
import net.slipp.domain.qna.Answer
import net.slipp.domain.qna.Question
import net.slipp.domain.qna.TemporaryAnswer
import net.slipp.domain.user.SocialUser
import net.slipp.service.qna.QnaService
import net.slipp.support.web.argumentresolver.LoginUser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import com.restfb.util.StringUtils
import com.typesafe.scalalogging.LazyLogging

@RestController
@RequestMapping(Array("/api/questions/{questionId}"))
class ApiQuestionController(@Resource(name = "qnaService") qnaService: QnaService) extends LazyLogging {
  @RequestMapping(Array("answers/logoutuser"))
  def answerWhenLogout(@PathVariable questionId: Long, contents: String, session: HttpSession) = {
    val answer = new TemporaryAnswer(questionId, contents)
    logger.debug("TemporaryAnswer : {}", answer)
    session.setAttribute(TemporaryAnswer.TEMPORARY_ANSWER_KEY, answer)
    true
  }

  @RequestMapping(value = Array("/like"), method = Array(RequestMethod.POST))
  def likeQuestions(@LoginUser loginUser: SocialUser, @PathVariable questionId: Long) = {
    val question = qnaService.likeQuestion(loginUser, questionId)
    question.getSumLike
  }

  @RequestMapping(value = Array("/detagged/{name}"), method = Array(RequestMethod.POST))
  def detagged(@LoginUser loginUser: SocialUser, @PathVariable questionId: Long, @PathVariable name: String) = {
    if (StringUtils.isBlank(name))
      false
    else {
      qnaService.detagged(loginUser, questionId, name)
      true
    }
  }

  @RequestMapping(value = Array("/answers/{answerId}/like"), method = Array(RequestMethod.POST))
  def likeAnswer(@LoginUser loginUser: SocialUser, @PathVariable questionId: Long, @PathVariable answerId: Long) = {
    val answer = qnaService.likeAnswer(loginUser, answerId)
    answer.getSumLike
  }

  @RequestMapping(value = Array("/answers/{answerId}/dislike"), method = Array(RequestMethod.POST))
  def dislikeAnswer(@LoginUser loginUser: SocialUser, @PathVariable questionId: Long, @PathVariable answerId: Long) = {
    val answer = qnaService.dislikeAnswer(loginUser, answerId)
    answer.getSumDislike
  }

  def this() = this(null)
}