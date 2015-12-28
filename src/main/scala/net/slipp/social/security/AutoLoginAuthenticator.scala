package net.slipp.social.security

import javax.annotation.Resource
import net.slipp.domain.ProviderType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.RememberMeServices
import org.springframework.util.Assert

class AutoLoginAuthenticator {
  @Resource(name = "authenticationManager") private var authenticationManager: AuthenticationManager = _
  @Resource(name = "slippRememberMeServices") private var rememberMeServices: RememberMeServices = _

  def login(email: String, password: String) {
    Assert.notNull(email, "UserId cannot be null!")
    Assert.notNull(password, "Password cannot be null!")
    val authRequest: UsernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, password)
    authRequest.setDetails(ProviderType.slipp)
    val successfulAuthentication: Authentication = authenticationManager.authenticate(authRequest)
    SecurityContextHolder.getContext.setAuthentication(successfulAuthentication)
  }
}
