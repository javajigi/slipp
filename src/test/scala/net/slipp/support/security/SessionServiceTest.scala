package net.slipp.support.security

import net.slipp.domain.user.SocialUserBuilder.aSocialUser
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.mockito.Mockito.when
import net.slipp.domain.user.SocialUser
import net.slipp.service.user.SocialUserService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl

@RunWith(classOf[MockitoJUnitRunner])
class SessionServiceTest {
  @Mock private[security] var socialUserService: SocialUserService = null
  @InjectMocks private var dut: SessionService = new SessionService

  @Test def getSocialUserWhenLoginFail {
    setupLoginUser(null)
    val actual: SocialUser = dut.getLoginUser
    assertThat(actual, is(SocialUser.GUEST_USER))
  }

  @Test def getSocialUserWhenLoginSuccess {
    val userId: String = "loginId"
    setupLoginUser(createAuthorizedAuthentication(userId))
    val socialUser: SocialUser = aSocialUser.withUserId(userId).build
    when(socialUserService.findByUserId("loginId")).thenReturn(socialUser)
    val actual: SocialUser = dut.getLoginUser
    assertThat(actual, is(socialUser))
  }

  private def setupLoginUser(authentication: Authentication) {
    val context: SecurityContext = new SecurityContextImpl
    context.setAuthentication(authentication)
    SecurityContextHolder.setContext(context)
  }

  private def createAuthorizedAuthentication(userId: String): Authentication = {
    val authentication: TestingAuthenticationToken = new TestingAuthenticationToken(userId, "password")
    authentication.setAuthenticated(true)
    return authentication
  }
}
