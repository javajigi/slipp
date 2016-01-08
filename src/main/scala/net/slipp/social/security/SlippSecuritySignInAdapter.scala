package net.slipp.social.security

import javax.annotation.Resource
import net.slipp.domain.qna.TemporaryAnswer
import net.slipp.domain.user.SocialUser
import net.slipp.service.user.SocialUserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionKey
import org.springframework.social.connect.web.SignInAdapter
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestAttributes

object SlippSecuritySignInAdapter {
  val SIGN_IN_DETAILS_SESSION_ATTRIBUTE_NAME: String = "net.slipp.social.security.signInDetails"
}

class SlippSecuritySignInAdapter extends SignInAdapter {
  private var log: Logger = LoggerFactory.getLogger(classOf[SlippSecuritySignInAdapter])

  @Resource(name = "socialUserService") private var socialUserService: SocialUserService = null

  def signIn(localUserId: String, connection: Connection[_], nativeWebRequest: NativeWebRequest): String = {
    val connectionKey: ConnectionKey = connection.getKey
    val socialUser: SocialUser = socialUserService.findByUserIdAndConnectionKey(localUserId, connectionKey)
    nativeWebRequest.setAttribute(SlippSecuritySignInAdapter.SIGN_IN_DETAILS_SESSION_ATTRIBUTE_NAME, socialUser, RequestAttributes.SCOPE_SESSION)
    val temporaryAnswer: TemporaryAnswer = getTemporaryAnswer(nativeWebRequest)
    return SlippSecurityAuthenticationFilter.DEFAULT_AUTHENTICATION_URL + "?redirect=" + temporaryAnswer.generateUrl
  }

  private def getTemporaryAnswer(nativeWebRequest: NativeWebRequest): TemporaryAnswer = {
    val value: AnyRef = nativeWebRequest.getAttribute(TemporaryAnswer.TEMPORARY_ANSWER_KEY, RequestAttributes.SCOPE_SESSION)
    log.debug("Temporary Answer : {}", value)
    if (value == null) {
      return TemporaryAnswer.EMPTY_ANSWER
    }
    return value.asInstanceOf[TemporaryAnswer]
  }
}
