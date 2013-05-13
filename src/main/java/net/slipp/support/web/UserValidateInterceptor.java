package net.slipp.support.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.slipp.domain.user.SocialUser;
import net.slipp.support.security.SessionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class UserValidateInterceptor extends HandlerInterceptorAdapter {
    private static Logger log = LoggerFactory.getLogger(UserValidateInterceptor.class);
    
    @Resource (name = "sessionService")
    private SessionService sessionService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("start user validate!");
        Object user = request.getAttribute(GlobalRequestAttributesInterceptor.DEFAULT_LOGIN_USER_REQUEST_KEY);
        if (user == null) {
            return super.preHandle(request, response, handler);
        }
        
        SocialUser socialUser = (SocialUser)user;
        if (socialUser.isGuest()) {
            return super.preHandle(request, response, handler);
        }
        return super.preHandle(request, response, handler);
    }
}
