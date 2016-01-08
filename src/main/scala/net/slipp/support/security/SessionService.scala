package net.slipp.support.security

import javax.annotation.Resource
import net.slipp.domain.ProviderType
import net.slipp.domain.user.SocialUser
import net.slipp.service.user.SocialUserService
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

/**
  * 로그인 사용자를 관리한다.
  */
@Service("sessionService")
class SessionService {
  @Resource(name = "socialUserService") private var socialUserService: SocialUserService = null

  def getLoginUser: SocialUser = {
    if (!isAuthenticated) {
      return SocialUser.GUEST_USER
    }
    var socialUser: SocialUser = null
    if (isSlippUser) {
      socialUser = socialUserService.findByEmail(getAuthenticatedUserName)
    }
    else {
      socialUser = socialUserService.findByUserId(getAuthenticatedUserName)
    }
    if (socialUser == null) {
      return SocialUser.GUEST_USER
    }
    return socialUser
  }

  def isAuthenticated: Boolean = {
    return if (getAuthentication == null) false else getAuthentication.isAuthenticated
  }

  def isSlippUser: Boolean = {
    val authentication: Authentication = getAuthentication
    val details: AnyRef = authentication.getDetails
    if (!(details.isInstanceOf[ProviderType])) {
      return false
    }
    val providerType: ProviderType = details.asInstanceOf[ProviderType]
    if (providerType eq ProviderType.slipp) {
      return true
    }
    return false
  }

  def getAuthentication: Authentication = {
    return SecurityContextHolder.getContext.getAuthentication
  }

  private def getAuthenticatedUserName: String = {
    return if (getAuthentication == null) null else getAuthentication.getName
  }
}
