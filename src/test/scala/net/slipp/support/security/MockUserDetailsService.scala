package net.slipp.support.security

import java.util.{ArrayList, List}

import net.slipp.domain.user.SocialUser
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.{User, UserDetails, UserDetailsService}

abstract class MockUserDetailsService extends UserDetailsService {
  private var log: Logger = LoggerFactory.getLogger(classOf[MockUserDetailsService])

  def loadUserByUsername(username: String): UserDetails = {
    val socialUser: SocialUser = createSocialUser
    return new User(username, socialUser.getPassword, true, true, true, true, createGrantedAuthorities(username))
  }

  protected def createSocialUser: SocialUser

  private def createGrantedAuthorities(username: String): List[GrantedAuthority] = {
    log.debug("UserName : {}", username)
    val grantedAuthorities: List[GrantedAuthority] = new ArrayList[GrantedAuthority]
    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"))
    return grantedAuthorities
  }
}
