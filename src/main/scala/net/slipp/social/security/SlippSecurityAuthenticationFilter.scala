package net.slipp.social.security

import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import net.slipp.domain.user.SocialUser
import net.slipp.support.security.SessionService
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.{AnonymousAuthenticationToken, AuthenticationManager, UsernamePasswordAuthenticationToken}
import org.springframework.security.core.{Authentication, AuthenticationException}
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.{AbstractAuthenticationProcessingFilter, RememberMeServices}

object SlippSecurityAuthenticationFilter {
  val DEFAULT_AUTHENTICATION_URL: String = "/authenticate"
}

class SlippSecurityAuthenticationFilter extends AbstractAuthenticationProcessingFilter(SlippSecurityAuthenticationFilter.DEFAULT_AUTHENTICATION_URL) {
  private var log: Logger = LoggerFactory.getLogger(classOf[SlippSecurityAuthenticationFilter])

  @Autowired private var sessionService: SessionService = null
  @Autowired private var userDetailsService: UserDetailsService = null

  @Autowired override def setAuthenticationManager(authenticationManager: AuthenticationManager) {
    super.setAuthenticationManager(authenticationManager)
  }

  override def setRememberMeServices(rememberMeServices: RememberMeServices) {
    super.setRememberMeServices(rememberMeServices)
  }

  @throws(classOf[AuthenticationException])
  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  def attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication = {
    log.debug("redirect url : {}", request.getAttribute("redirect"))
    if (sessionService.isAuthenticated) {
      log.debug("already authentication userId is : {}", sessionService.getAuthentication.getPrincipal)
      return sessionService.getAuthentication
    }
    else {
      val signInDetails: SocialUser = request.getSession.getAttribute(SlippSecuritySignInAdapter.SIGN_IN_DETAILS_SESSION_ATTRIBUTE_NAME).asInstanceOf[SocialUser]
      if (signInDetails == null) {
        log.debug("sns login failed. so login anonymous!")
        return new AnonymousAuthenticationToken("slippAnonymousAuthenticationToken", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"))
      }
      log.debug("sns login success. login userId : {}", signInDetails.getUserId)
      var userDetails: SlippUser = null
      if (signInDetails.isSLiPPUser) {
        val slippUserDetailsService: SlippUserDetailsService = userDetailsService.asInstanceOf[SlippUserDetailsService]
        userDetails = slippUserDetailsService.loadUserByEmail(signInDetails.getUserId).asInstanceOf[SlippUser]
      }
      else {
        userDetails = userDetailsService.loadUserByUsername(signInDetails.getUserId).asInstanceOf[SlippUser]
      }
      val authenticationToken: UsernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername, userDetails.getPassword, userDetails.getAuthorities)
      authenticationToken.setDetails(userDetails.getProviderType)
      return authenticationToken
    }
  }
}
