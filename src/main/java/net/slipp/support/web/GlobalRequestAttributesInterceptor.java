package net.slipp.support.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.slipp.domain.user.SocialUser;
import net.slipp.service.qna.NotificationService;
import net.slipp.support.security.SessionService;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class GlobalRequestAttributesInterceptor extends HandlerInterceptorAdapter {
	@Resource (name = "sessionService")
	private SessionService sessionService;
	
	@Resource (name = "notificationService")
	private NotificationService notificationService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		SocialUser socialUser = sessionService.getLoginUser();
		request.setAttribute("loginUser", socialUser);
		
		if (!socialUser.isGuest()) {
			request.setAttribute("notifications", notificationService.findNotifications(socialUser));
		}
		return super.preHandle(request, response, handler);
	}
}
