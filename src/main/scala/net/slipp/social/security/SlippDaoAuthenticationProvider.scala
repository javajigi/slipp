package net.slipp.social.security

import net.slipp.domain.ProviderType
import org.springframework.security.authentication.{AuthenticationServiceException, BadCredentialsException, UsernamePasswordAuthenticationToken}
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.{Authentication, AuthenticationException}
import org.springframework.security.core.authority.mapping.{GrantedAuthoritiesMapper, NullAuthoritiesMapper}
import org.springframework.security.core.userdetails.{UserDetails, UserDetailsService, UsernameNotFoundException}
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.util.Assert

class SlippDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
  private var passwordEncoder: PasswordEncoder = null
  private var userDetailsService: UserDetailsService = null
  private var authoritiesMapper: GrantedAuthoritiesMapper = new NullAuthoritiesMapper

  protected def additionalAuthenticationChecks(userDetails: UserDetails, authentication: UsernamePasswordAuthenticationToken) {
    if (authentication.getCredentials == null) {
      logger.debug("Authentication failed: no credentials provided")
      throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"))
    }
    val presentedPassword: String = authentication.getCredentials.toString
    if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword)) {
      logger.debug("Authentication failed: password does not match stored value")
      throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"))
    }
  }

  protected override def createSuccessAuthentication(principal: AnyRef, authentication: Authentication, user: UserDetails): Authentication = {
    val result: UsernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(principal, authentication.getCredentials, authoritiesMapper.mapAuthorities(user.getAuthorities))
    result.setDetails(ProviderType.slipp)
    result
  }

  protected override def doAfterPropertiesSet {
    Assert.notNull(this.userDetailsService, "A UserDetailsService must be set")
  }

  protected final def retrieveUser(email: String, authentication: UsernamePasswordAuthenticationToken): UserDetails = {
    var loadedUser: UserDetails = null
    try {
      val slippUserDetailsService: SlippUserDetailsService = getUserDetailsService.asInstanceOf[SlippUserDetailsService]
      loadedUser = slippUserDetailsService.loadUserByEmail(email)
    }
    catch {
      case notFound: UsernameNotFoundException => {
        throw notFound
      }
      case repositoryProblem: Exception => {
        throw new AuthenticationServiceException(repositoryProblem.getMessage, repositoryProblem)
      }
    }
    if (loadedUser == null) {
      throw new AuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation")
    }
    return loadedUser
  }

  def setPasswordEncoder(passwordEncoder: PasswordEncoder) {
    Assert.notNull(passwordEncoder, "passwordEncoder cannot be null")
    this.passwordEncoder = passwordEncoder
  }

  protected def getPasswordEncoder: PasswordEncoder = {
    return passwordEncoder
  }

  def setUserDetailsService(userDetailsService: UserDetailsService) {
    this.userDetailsService = userDetailsService
  }

  protected def getUserDetailsService: UserDetailsService = {
    return userDetailsService
  }
}