package net.slipp.social.security

import net.slipp.domain.user.ExistedUserException
import net.slipp.service.user.SocialUserService
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionData
import org.springframework.social.connect.web.ProviderSignInUtils
import org.springframework.social.connect.web.SignInAdapter
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.context.request.ServletWebRequest
import javax.annotation.Resource

@Controller
@RequestMapping(Array("/signup")) class SlippSecuritySignUpController {
  private var authenticateUrl: String = SlippSecurityAuthenticationFilter.DEFAULT_AUTHENTICATION_URL

  @Resource(name = "socialUserService") private var socialUserService: SocialUserService = null
  @Resource(name = "signInAdapter") private var signInAdapter: SignInAdapter = null
  @Resource(name = "providerSignInUtils") private var providerSignInUtils: ProviderSignInUtils = null

  @RequestMapping(value = Array(""), method = Array(RequestMethod.GET)) def signUpForm(request: ServletWebRequest, model: Model): String = {
    val connection: Connection[_] = providerSignInUtils.getConnectionFromSession(request)
    val connectionData: ConnectionData = connection.createData
    val signUpForm: SignUpForm = new SignUpForm(connectionData.getDisplayName)
    model.addAttribute("signUpForm", signUpForm)
    return "users/signUpForm"
  }

  @RequestMapping(value = Array(""), method = Array(RequestMethod.POST)) def signUpSubmit(request: ServletWebRequest, signUpForm: SignUpForm, result: BindingResult): String = {
    val connection: Connection[_] = providerSignInUtils.getConnectionFromSession(request)
    try {
      socialUserService.createNewSocialUser(signUpForm.getUserId, connection)
      signInAdapter.signIn(signUpForm.getUserId, connection, request)
      s"redirect:${authenticateUrl}"
    }
    catch {
      case e: ExistedUserException => {
        result.addError(new FieldError("signUpForm", "userId", signUpForm.getUserId + "은 이미 존재하는 아이디입니다. 다른 아이디를 선택해주세요."))
        return "users/signUpForm"
      }
    }
  }
}
