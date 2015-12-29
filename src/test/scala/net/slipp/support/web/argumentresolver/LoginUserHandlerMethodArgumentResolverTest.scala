package net.slipp.support.web.argumentresolver

import net.slipp.domain.user.SocialUser
import net.slipp.support.security.{LoginRequiredException, SessionService}
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.junit.{Before, Test}
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.when
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.core.MethodParameter
import org.springframework.web.context.request.NativeWebRequest

@RunWith(classOf[MockitoJUnitRunner])
class LoginUserHandlerMethodArgumentResolverTest {
  @Mock private var parameter: MethodParameter = null
  @Mock private var webRequest: NativeWebRequest = null
  @Mock private var sessionService: SessionService = null
  @Mock private var user: SocialUser = null
  private var loginUserHandlerMethodArgumentResolver: LoginUserHandlerMethodArgumentResolver = null

  @Before
  def setUp {
    loginUserHandlerMethodArgumentResolver = new LoginUserHandlerMethodArgumentResolver
    loginUserHandlerMethodArgumentResolver.setSessionService(sessionService)
    when(user.isGuest).thenReturn(false)
  }

  @Test def supportsParameter_false {
    when(parameter.hasParameterAnnotation(classOf[LoginUser])).thenReturn(false)
    assertThat(loginUserHandlerMethodArgumentResolver.supportsParameter(parameter), is(false))
  }

  @Test def supportsParameter_true {
    when(parameter.hasParameterAnnotation(classOf[LoginUser])).thenReturn(true)
    assertThat(loginUserHandlerMethodArgumentResolver.supportsParameter(parameter), is(true))
  }

  @Test(expected = classOf[LoginRequiredException])
  @throws(classOf[Exception])
  def loginUserRequired_but_guest {
    when(parameter.getParameterAnnotation(classOf[LoginUser])).thenReturn(new FakeLoginUser(true))
    when(sessionService.getLoginUser).thenReturn(SocialUser.GUEST_USER)
    loginUserHandlerMethodArgumentResolver.resolveArgument(parameter, null, webRequest, null)
  }

  @Test
  @throws(classOf[Exception])
  def loginUser_not_required {
    when(parameter.getParameterAnnotation(classOf[LoginUser])).thenReturn(new FakeLoginUser(false))
    when(sessionService.getLoginUser).thenReturn(SocialUser.GUEST_USER)
    val loginUser: SocialUser = loginUserHandlerMethodArgumentResolver.resolveArgument(parameter, null, webRequest, null).asInstanceOf[SocialUser]
    assertThat(loginUser, is(SocialUser.GUEST_USER))
  }

  @Test
  def loginUser_일반상황 {
    val user: SocialUser = new SocialUser
    when(parameter.getParameterAnnotation(classOf[LoginUser])).thenReturn(new FakeLoginUser(true))
    when(sessionService.getLoginUser).thenReturn(user)
    val loginUser: SocialUser = loginUserHandlerMethodArgumentResolver.resolveArgument(parameter, null, webRequest, null).asInstanceOf[SocialUser]
    assertThat(loginUser, is(user))
  }
}

