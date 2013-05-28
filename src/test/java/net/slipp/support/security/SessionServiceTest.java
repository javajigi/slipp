package net.slipp.support.security;

import static net.slipp.domain.user.SocialUserBuilder.aSocialUser;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import net.slipp.domain.user.SocialUser;
import net.slipp.service.user.SocialUserService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

@RunWith(MockitoJUnitRunner.class)
public class SessionServiceTest {
    @Mock 
    SocialUserService socialUserService;
    
    @InjectMocks
    private SessionService dut = new SessionService();
    
    @Test
    public void getSocialUserWhenLoginFail() {
        setupLoginUser(null);
        
        SocialUser actual = dut.getLoginUser();
        assertThat(actual, is(SocialUser.GUEST_USER));
    }

    @Test
    public void getSocialUserWhenLoginSuccess() {
        String userId = "loginId";
        setupLoginUser(createAuthorizedAuthentication(userId));
        
        SocialUser socialUser = aSocialUser().withUserId(userId).build();
        when(socialUserService.findByUserId("loginId")).thenReturn(socialUser);
        SocialUser actual = dut.getLoginUser();
        assertThat(actual, is(socialUser));
    }

    private void setupLoginUser(Authentication authentication) {
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthorizedAuthentication(String userId) {
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(userId, "password");
        authentication.setAuthenticated(true);
        return authentication;
    }
}
