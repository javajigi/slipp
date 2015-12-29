package net.slipp.support.security

import com.google.common.collect.Lists
import net.slipp.domain.user.{SocialUser, SocialUserBuilder}
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.junit.{Before, Test}
import org.springframework.security.authentication.{AuthenticationManager, AuthenticationProvider, ProviderManager, RememberMeAuthenticationProvider, RememberMeAuthenticationToken, UsernamePasswordAuthenticationToken}
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.{UserDetails, UserDetailsService}

class AuthenticationManagerTest {
  private var dut: AuthenticationManager = null
  private var socialUser: SocialUser = null
  private var userDetailsService: UserDetailsService = null

  @Before def setup {
    socialUser = new SocialUserBuilder().withUserId("userId").withRawPassword("password").build
    prepareAuthenticationManager
  }

  private def prepareAuthenticationManager {
    userDetailsService = new MockUserDetailsService() {
      protected def createSocialUser: SocialUser = {
        return socialUser
      }
    }
    val daoProvider: DaoAuthenticationProvider = new DaoAuthenticationProvider
    daoProvider.setUserDetailsService(userDetailsService)
    val rememberMeProvider: RememberMeAuthenticationProvider = new RememberMeAuthenticationProvider("key-1234")
    val providers = Lists.newArrayList[AuthenticationProvider]
    providers.add(daoProvider)
    providers.add(rememberMeProvider)
    dut = new ProviderManager(providers)
  }

  @Test
  @throws(classOf[Exception])
  def authenticate_daoAuthenticationProvider {
    val authRequest: UsernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(socialUser.getUserId, socialUser.getPassword)
    val authResult: Authentication = dut.authenticate(authRequest)
    assertThat(authResult.isAuthenticated, is(true))
  }

  @Test
  @throws(classOf[Exception])
  def authenticate_remembermeAuthenticationProvider {
    val userDetails: UserDetails = userDetailsService.loadUserByUsername(socialUser.getUserId)
    val authRequest: RememberMeAuthenticationToken = new RememberMeAuthenticationToken("key-1234", userDetails, userDetails.getAuthorities)
    val authResult: Authentication = dut.authenticate(authRequest)
    assertThat(authResult.isAuthenticated, is(true))
  }
}
