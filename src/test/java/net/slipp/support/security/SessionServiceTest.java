package net.slipp.support.security;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import net.slipp.domain.user.SocialUser;
import net.slipp.repository.user.SocialUserBuilder;
import net.slipp.repository.user.SocialUserRepository;

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
    SocialUserRepository socialUserRepository;
    
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
        
        SocialUser socialUser = new SocialUserBuilder().userId("loginId").build();
        List<SocialUser> socialUsers = Arrays.asList(socialUser);
        
        when(socialUserRepository.findsByUserId("loginId")).thenReturn(socialUsers);
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
