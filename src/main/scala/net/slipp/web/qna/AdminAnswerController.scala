package net.slipp.web.qna

import javax.annotation.Resource

import net.slipp.domain.qna.Answer
import net.slipp.domain.user.SocialUser
import net.slipp.service.qna.QnaService
import net.slipp.service.tag.TagService
import net.slipp.support.web.argumentresolver.LoginUser

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
@RequestMapping(Array("/admin/questions/{questionId}/answers"))
class AdminAnswerController(@Resource(name = "qnaService") qnaService: QnaService) {

  @RequestMapping(Array("{answerId}/form"))
  def updateForm(@LoginUser loginUser: SocialUser, @PathVariable questionId: Long, @PathVariable answerId: Long, searchTerm: String, model: Model) = {
    model.addAttribute("question", qnaService.findByQuestionId(questionId))
    model.addAttribute("answer", qnaService.findAnswerById(answerId))
    model.addAttribute("searchTerm", searchTerm)
    "admin/qna/answer"
  }

  @RequestMapping(value = Array("{answerId}"), method = Array(RequestMethod.PUT))
  def update(@LoginUser loginUser: SocialUser, @PathVariable questionId: Long, @PathVariable answerId: Long, answer: Answer) = {
    val originalAnswer = qnaService.findAnswerById(answerId)
    qnaService.updateAnswer(originalAnswer.getWriter, answer)
    "redirect:/admin/questions/%d#answer-%d".format(questionId, answerId)
  }

  def this() = this(null)
}