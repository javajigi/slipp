package net.slipp.support.web

import javax.annotation.Resource
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import net.slipp.domain.user.SocialUser
import net.slipp.service.qna.NotificationService
import net.slipp.support.security.SessionService
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

object GlobalRequestAttributesInterceptor {
  val DEFAULT_LOGIN_USER_REQUEST_KEY: String = "loginUser"
}

class GlobalRequestAttributesInterceptor extends HandlerInterceptorAdapter {
  @Resource(name = "sessionService") private var sessionService: SessionService = null
  @Resource(name = "notificationService") private var notificationService: NotificationService = null

  @throws(classOf[Exception])
  override def preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: AnyRef): Boolean = {
    val loginUser: SocialUser = sessionService.getLoginUser
    request.setAttribute(GlobalRequestAttributesInterceptor.DEFAULT_LOGIN_USER_REQUEST_KEY, loginUser)
    if (!loginUser.isGuest) {
      request.setAttribute("countNotifications", notificationService.countByNotifiee(loginUser))
    }
    return super.preHandle(request, response, handler)
  }
}
