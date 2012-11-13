package net.slipp.support.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.slipp.support.security.SessionService;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class GlobalRequestAttributesInterceptor extends HandlerInterceptorAdapter {
	@Resource (name = "sessionService")
	private SessionService sessionService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setAttribute("loginUser", sessionService.getLoginUser());
		
		return super.preHandle(request, response, handler);
	}
}
