package net.slipp.support.web.argumentresolver

import javax.annotation.Resource
import net.slipp.domain.user.SocialUser
import net.slipp.support.security.LoginRequiredException
import net.slipp.support.security.SessionService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

/**
  * {@link LoginUser} 어노테이션이 있는 컨트롤러 메소드에 로그인 사용자 객체를 주입해준다.
  */
class LoginUserHandlerMethodArgumentResolver extends HandlerMethodArgumentResolver {
  private var log: Logger = LoggerFactory.getLogger(classOf[LoginUserHandlerMethodArgumentResolver])
  @Resource(name = "sessionService") private var sessionService: SessionService = null

  def setSessionService(sessionService: SessionService) {
    this.sessionService = sessionService
  }

  def supportsParameter(parameter: MethodParameter): Boolean = {
    return parameter.hasParameterAnnotation(classOf[LoginUser])
  }

  @throws(classOf[Exception])
  def resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory): AnyRef = {
    val loginUserAnnotation: LoginUser = parameter.getParameterAnnotation(classOf[LoginUser])
    val loginUser: SocialUser = sessionService.getLoginUser
    log.debug("@LoginUser : {}", loginUser)
    if (loginUserAnnotation.required && loginUser.isGuest) {
      throw new LoginRequiredException
    }
    return loginUser
  }
}
