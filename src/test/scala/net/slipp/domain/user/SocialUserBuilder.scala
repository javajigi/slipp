package net.slipp.domain.user

import net.slipp.domain.ProviderType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object SocialUserBuilder {
  def aSocialUser: SocialUserBuilder = {
    return new SocialUserBuilder
  }
}

class SocialUserBuilder {
  private var id: Long = 0L
  private var userId: String = null
  private var providerUserId: String = null
  private var accessToken: String = null
  private var rank: Int = 0
  private var displayName: String = null
  private var profileUrl: String = null
  private var imageUrl: String = null
  private var rawPassword: String = null
  private var password: String = null
  private var email: String = null
  private var providerType: ProviderType = ProviderType.facebook

  def withId(id: Long): SocialUserBuilder = {
    this.id = id
    return this
  }

  def withUserId(userId: String): SocialUserBuilder = {
    this.userId = userId
    return this
  }

  def withRawPassword(rawPassword: String): SocialUserBuilder = {
    val encoder: BCryptPasswordEncoder = new BCryptPasswordEncoder
    this.rawPassword = rawPassword
    this.password = encoder.encode(rawPassword)
    return this
  }

  def withAccessToken(accessToken: String): SocialUserBuilder = {
    this.accessToken = accessToken
    return this
  }

  def withProviderType(providerType: ProviderType): SocialUserBuilder = {
    this.providerType = providerType
    return this
  }

  def withDisplayName(displayName: String): SocialUserBuilder = {
    this.displayName = displayName
    return this
  }

  def withRank(rank: Int): SocialUserBuilder = {
    this.rank = rank
    return this
  }

  def withEmail(email: String): SocialUserBuilder = {
    this.email = email
    return this
  }

  def createTestUser(userId: String): SocialUser = {
    withUserId(userId)
    withAccessToken("1234-5678" + userId)
    withProviderType(ProviderType.facebook)
    return build
  }

  def build: SocialUser = {
    val socialUser: SocialUser = new SocialUser
    socialUser.setId(id)
    socialUser.setUserId(userId)
    socialUser.setProviderId(providerType.name)
    socialUser.setProviderUserId(providerUserId)
    socialUser.setAccessToken(accessToken)
    socialUser.setRank(rank)
    socialUser.setDisplayName(displayName)
    socialUser.setProfileUrl(profileUrl)
    socialUser.setImageUrl(imageUrl)
    socialUser.setPassword(password)
    socialUser.setRawPassword(rawPassword)
    socialUser.setEmail(email)
    return socialUser
  }
}
