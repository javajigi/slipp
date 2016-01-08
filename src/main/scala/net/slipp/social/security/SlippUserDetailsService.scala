package net.slipp.social.security

import java.util.{ArrayList, Arrays, List}
import javax.annotation.Resource

import net.slipp.domain.user.SocialUser
import net.slipp.service.user.SocialUserService
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.{UserDetails, UserDetailsService, UsernameNotFoundException}

class SlippUserDetailsService extends UserDetailsService {
  private val logger: Logger = LoggerFactory.getLogger(classOf[SlippUserDetailsService])

  @Resource(name = "socialUserService") private var socialUserService: SocialUserService = null
  private var adminUsers: String = null

  def setAdminUsers(adminUsers: String) {
    this.adminUsers = adminUsers
  }

  @throws(classOf[UsernameNotFoundException])
  def loadUserByUsername(username: String): UserDetails = {
    val socialUser: SocialUser = socialUserService.findByUserId(username)
    if (socialUser == null) {
      throw new UsernameNotFoundException(String.format("%s not found!", username))
    }
    if (socialUser.isBlocked) {
      return new SlippUser(username, socialUser.getPassword, socialUser.getProviderIdBySnsType, createEmptyGrantedAuthorities)
    }
    return new SlippUser(username, socialUser.getAccessToken, socialUser.getProviderIdBySnsType, createGrantedAuthorities(username, socialUser.isAdmined))
  }

  @throws(classOf[UsernameNotFoundException])
  def loadUserByEmail(email: String): UserDetails = {
    val socialUser: SocialUser = socialUserService.findByEmail(email)
    if (socialUser == null) {
      throw new UsernameNotFoundException(String.format("%s not found!", email))
    }
    if (socialUser.isBlocked) {
      return new SlippUser(email, socialUser.getPassword, socialUser.getProviderIdBySnsType, createEmptyGrantedAuthorities)
    }
    return new SlippUser(email, socialUser.getPassword, socialUser.getProviderIdBySnsType, createGrantedAuthorities(email, socialUser.isAdmined))
  }

  private def createEmptyGrantedAuthorities: List[GrantedAuthority] = {
    return new ArrayList[GrantedAuthority]
  }

  private def createGrantedAuthorities(username: String, isAdmin: Boolean): List[GrantedAuthority] = {
    logger.debug("UserName : {}", username)
    val grantedAuthorities: List[GrantedAuthority] = new ArrayList[GrantedAuthority]
    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"))
    if (isAdmin || isRootAdmin(username)) {
      grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"))
    }
    return grantedAuthorities
  }

  def isRootAdmin(username: String): Boolean = {
    adminUsers.split(":").contains(username)
  }
}