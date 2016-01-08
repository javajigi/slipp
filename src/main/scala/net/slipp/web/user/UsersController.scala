package net.slipp.web.user

import java.net.URLEncoder

import javax.annotation.Resource

import net.slipp.domain.user.PasswordDto
import net.slipp.domain.user.SocialUser
import net.slipp.service.qna.QnaService
import net.slipp.service.user.SocialUserService
import net.slipp.social.security.AutoLoginAuthenticator
import net.slipp.support.web.argumentresolver.LoginUser
import net.slipp.web.UserForm
import net.slipp.web.QnAPageableHelper._

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
@RequestMapping(Array("/users"))
class UsersController(
  @Resource(name = "socialUserService") userService: SocialUserService,
  @Resource(name = "autoLoginAuthenticator") autoLoginAuthenticator: AutoLoginAuthenticator,
  @Resource(name = "qnaService") qnaService: QnaService) {
  private val DefaultSummaryPageSize = 5
  private val DefaultPageSize = 15

  @RequestMapping(Array("/login"))
  def login(model: Model) = {
    model.addAttribute("user", new UserForm())
    "users/login"
  }

  @RequestMapping(value = Array(""), method = Array(RequestMethod.POST))
  def create(user: UserForm, redirect: String) = {
    val socialUser = userService.createSlippUser(user.getUserId(), user.getEmail())
    autoLoginAuthenticator.login(socialUser.getEmail, socialUser.getRawPassword)
    "redirect:%s".format(redirect)
  }

  @RequestMapping(Array("/fblogout"))
  def logout = "users/fblogout"

  @RequestMapping(Array("/{id}"))
  def profileById(@PathVariable id: Long) = {
    val socialUser = userService.findById(id)
    "redirect:/users/%d/%s".format(id, URLEncoder.encode(socialUser.getUserId, "UTF-8"))
  }

  @RequestMapping(Array("/{id}/{userId}"))
  def profile(@PathVariable id: Long, @PathVariable userId: String, model: Model) = {
    model.addAttribute("questions", qnaService.findsQuestionByWriter(Option(id), createPageableByQuestionUpdatedDate(DefaultPageNo, DefaultSummaryPageSize)))
    model.addAttribute("answers", qnaService.findsAnswerByWriter(id, createPageableByAnswer(DefaultPageNo, DefaultSummaryPageSize)))
    model.addAttribute("socialUser", userService.findById(id))
    "users/profile"
  }

  @RequestMapping(Array("/{id}/{userId}/questions"))
  def questions(@PathVariable id: Long, page: Integer, model: Model) = {
    model.addAttribute("questions", qnaService.findsQuestionByWriter(Option(id), createPageableByQuestionUpdatedDate(revisedPage(page), DefaultPageSize)))
    model.addAttribute("socialUser", userService.findById(id))
    "users/questions"
  }

  @RequestMapping(Array("/{id}/{userId}/answers"))
  def answers(@PathVariable id: Long, page: Integer, model: Model) = {
    model.addAttribute("answers", qnaService.findsAnswerByWriter(id, createPageableByAnswer(revisedPage(page), DefaultPageSize)))
    model.addAttribute("socialUser", userService.findById(id))
    "users/answers"
  }

  @RequestMapping(Array("{id}/form"))
  def updateForm(@LoginUser loginUser: SocialUser, @PathVariable id: Long, model: Model) = {
    val socialUser = userService.findById(id)
    if (!loginUser.isSameUser(socialUser)) {
      throw new IllegalArgumentException("You cann't change another user!")
    }

    model.addAttribute("user", new UserForm(socialUser.getUserId, socialUser.getEmail))
    model.addAttribute("socialUser", socialUser)
    "users/form"
  }

  @RequestMapping(value = Array("{id}"), method = Array(RequestMethod.PUT))
  def update(@LoginUser loginUser: SocialUser, @PathVariable id: Long, userForm: UserForm) = {
    val socialUser = userService.findById(id)
    if (!loginUser.isSameUser(socialUser)) {
      throw new IllegalArgumentException("You cann't change another user!")
    }
    userService.updateSlippUser(loginUser, userForm.getEmail(), userForm.getUserId())
    "redirect:/users/logout"
  }

  @RequestMapping(Array("{id}/changepassword"))
  def changePasswordForm(@LoginUser loginUser: SocialUser, @PathVariable id: Long, model: Model) = {
    val socialUser = userService.findById(id)
    if (!loginUser.isSameUser(socialUser)) {
      throw new IllegalArgumentException("You cann't change another user!")
    }

    model.addAttribute("socialUser", socialUser)
    model.addAttribute("password", new PasswordDto(id))
    "users/changepassword"
  }

  @RequestMapping(value = Array("{id}/changepassword"), method = Array(RequestMethod.POST))
  def changePassword(@LoginUser loginUser: SocialUser, @PathVariable id: Long, password: PasswordDto, model: Model) = {
    val socialUser = userService.findById(id)
    if (!loginUser.isSameUser(socialUser)) {
      throw new IllegalArgumentException("You cann't change another user!")
    }

    try {
      userService.changePassword(loginUser, password)
      "redirect:/users/logout"
    } catch {
      case e: BadCredentialsException => {
          model.addAttribute("errorMessage", e.getMessage())
          model.addAttribute("socialUser", socialUser)
          model.addAttribute("password", new PasswordDto(id))
          "users/changepassword"
      }
    }
  }

  def this() = this(null, null, null)
}