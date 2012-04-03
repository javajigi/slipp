package net.slipp.support.web.argumentresolver;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;

import net.slipp.domain.user.SocialUser;
import net.slipp.support.security.LoginRequiredException;
import net.slipp.support.security.SessionService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;

@RunWith(MockitoJUnitRunner.class)
public class LoginUserHandlerMethodArgumentResolverTest {
    @Mock
    private MethodParameter parameter;

    @Mock
    private NativeWebRequest webRequest;

    @Mock
    private SessionService sessionService;

    @Mock
    private SocialUser user;

    private LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;

    @Before
    public void setUp() throws Exception {
        loginUserHandlerMethodArgumentResolver = new LoginUserHandlerMethodArgumentResolver();
        loginUserHandlerMethodArgumentResolver.setSessionService(sessionService);

        when(user.isGuest()).thenReturn(false);
    }

    @Test
    public void supportsParameter_false() {
        when(parameter.hasParameterAnnotation(LoginUser.class)).thenReturn(false);

        assertThat(loginUserHandlerMethodArgumentResolver.supportsParameter(parameter), is(false));
    }

    @Test
    public void supportsParameter_true() {
        when(parameter.hasParameterAnnotation(LoginUser.class)).thenReturn(true);

        assertThat(loginUserHandlerMethodArgumentResolver.supportsParameter(parameter), is(true));
    }

    @Test(expected = LoginRequiredException.class)
    public void loginUserRequired_but_guest() throws Exception {
        when(parameter.getParameterAnnotation(LoginUser.class)).thenReturn( new FakeLoginUser(true));
        when(sessionService.getLoginUser()).thenReturn(SocialUser.GUEST_USER);

        loginUserHandlerMethodArgumentResolver.resolveArgument(parameter, null, webRequest, null);
    }

    @Test
    public void loginUser_not_required() throws Exception {
        when(parameter.getParameterAnnotation(LoginUser.class)).thenReturn( new FakeLoginUser(false));
        when(sessionService.getLoginUser()).thenReturn(SocialUser.GUEST_USER);

        SocialUser loginUser = (SocialUser) loginUserHandlerMethodArgumentResolver.resolveArgument(parameter, null,
                webRequest, null);

        assertThat(loginUser, is(SocialUser.GUEST_USER));
    }

    @Test
    public void loginUser_일반상황() throws Exception {
        SocialUser user = new SocialUser();
        when(parameter.getParameterAnnotation(LoginUser.class)).thenReturn( new FakeLoginUser(true));
        when(sessionService.getLoginUser()).thenReturn(user);

        SocialUser loginUser = (SocialUser) loginUserHandlerMethodArgumentResolver.resolveArgument(parameter, null,
                webRequest, null);

        assertThat(loginUser, is(user));
    }
    
    public static class FakeLoginUser implements LoginUser {
        private boolean requiredValue = true;

        public FakeLoginUser(boolean requiredValue) {
            this.requiredValue = requiredValue;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return LoginUser.class;
        }

        @Override
        public boolean required() {
            return requiredValue;
        }
    }
}
