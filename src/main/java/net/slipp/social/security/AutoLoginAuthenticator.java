package net.slipp.social.security;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;

public class AutoLoginAuthenticator {
	@Resource(name="authenticationManager")
	AuthenticationManager authenticationManager;
	
	@Resource(name="rememberMeServices")
	RememberMeServices rememberMeServices;

	public void login(HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userId, password);
		Authentication successfulAuthentication = authenticationManager.authenticate(authRequest);
		rememberMeServices.loginSuccess(request, response, successfulAuthentication);
	}
}
