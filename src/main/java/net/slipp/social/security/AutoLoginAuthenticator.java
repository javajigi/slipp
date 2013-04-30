package net.slipp.social.security;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.slipp.domain.user.SocialUser;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;

public class AutoLoginAuthenticator {
    @Resource(name = "authenticationManager")
    private AuthenticationManager authenticationManager;

    @Resource(name = "slippRememberMeServices")
    private RememberMeServices rememberMeServices;

    public void login(HttpServletRequest request, HttpServletResponse response, SocialUser socialUser) {
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                socialUser.getUserId(), socialUser.getRawPassword());
        Authentication successfulAuthentication = authenticationManager.authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);
    }
}
