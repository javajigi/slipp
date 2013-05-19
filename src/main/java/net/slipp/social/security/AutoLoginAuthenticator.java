package net.slipp.social.security;

import javax.annotation.Resource;

import net.slipp.domain.ProviderType;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.util.Assert;

public class AutoLoginAuthenticator {
    @Resource(name = "authenticationManager")
    private AuthenticationManager authenticationManager;

    @Resource(name = "slippRememberMeServices")
    private RememberMeServices rememberMeServices;

    public void login(String email, String password) {
        Assert.notNull(email, "UserId cannot be null!");
        Assert.notNull(password, "Password cannot be null!");
        
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);
        authRequest.setDetails(ProviderType.slipp);
        Authentication successfulAuthentication = authenticationManager.authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);
    }
}
