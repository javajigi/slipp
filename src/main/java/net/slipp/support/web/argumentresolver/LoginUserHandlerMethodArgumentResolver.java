package net.slipp.support.web.argumentresolver;

import javax.annotation.Resource;

import net.slipp.domain.user.SocialUser;
import net.slipp.support.security.LoginRequiredException;
import net.slipp.support.security.SessionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * {@link LoginUser} 어노테이션이 있는 컨트롤러 메소드에 로그인 사용자 객체를 주입해준다.
 */
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
	private Logger log = LoggerFactory.getLogger(LoginUserHandlerMethodArgumentResolver.class);

	@Resource (name = "sessionService")
	private SessionService sessionService;

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(LoginUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		LoginUser loginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class);

		SocialUser loginUser = sessionService.getLoginUser();
		log.debug("@LoginUser : {}", loginUser);

		if (loginUserAnnotation.required() && loginUser.isGuest()) {
			throw new LoginRequiredException();
		}

		return loginUser;
	}
}
