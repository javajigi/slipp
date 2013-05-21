package net.slipp.support.security;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import net.slipp.domain.user.SocialUser;
import net.slipp.domain.user.SocialUserBuilder;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.google.common.collect.Lists;

public class AuthenticationManagerTest {
    private AuthenticationManager dut;
    
    private SocialUser socialUser;
    
    private UserDetailsService userDetailsService;

    @Before
    public void setup() {
        socialUser = new SocialUserBuilder()
            .withUserId("userId")
            .withRawPassword("password")
            .build();
        prepareAuthenticationManager();
    }

    private void prepareAuthenticationManager() {
        userDetailsService = new MockUserDetailsService() {
            protected SocialUser createSocialUser() {
                return socialUser;
            }
        };
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService);
        RememberMeAuthenticationProvider rememberMeProvider = new RememberMeAuthenticationProvider("key-1234");
        List<AuthenticationProvider> providers = Lists.newArrayList();
        providers.add(daoProvider);
        providers.add(rememberMeProvider);
        dut = new ProviderManager(providers);
    }

    @Test
    public void authenticate_daoAuthenticationProvider() throws Exception {
        UsernamePasswordAuthenticationToken authRequest = 
                new UsernamePasswordAuthenticationToken(socialUser.getUserId(), socialUser.getPassword());
        Authentication authResult = dut.authenticate(authRequest);
        assertThat(authResult.isAuthenticated(), is(true));
    }
    

    @Test
    public void authenticate_remembermeAuthenticationProvider() throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername(socialUser.getUserId());
        RememberMeAuthenticationToken authRequest = 
                new RememberMeAuthenticationToken("key-1234", userDetails, userDetails.getAuthorities());
        Authentication authResult = dut.authenticate(authRequest);
        assertThat(authResult.isAuthenticated(), is(true));
    }
}
