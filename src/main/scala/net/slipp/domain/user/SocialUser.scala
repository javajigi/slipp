package net.slipp.domain.user

import java.lang.Long
import net.slipp.domain.ProviderType
import net.slipp.support.jpa.DomainModel
import net.slipp.support.utils.MD5Util
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import javax.persistence._
import java.util.Date

class GuestSocialUser extends SocialUser {
  override def isGuest: Boolean = {
    return true
  }
}

object SocialUser {
  val GUEST_USER: SocialUser = new GuestSocialUser
  val DEFAULT_SLIPP_USER_PROFILE_SUFFIX: String = "http://www.gravatar.com/avatar/"
}

@Entity
@Table(uniqueConstraints = Array(new UniqueConstraint(columnNames = Array("userId", "providerId", "providerUserId")),
  new UniqueConstraint(columnNames = Array("userId", "providerId", "rank"))))
@Cache(region = "socialUser", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class SocialUser(i: Long) extends DomainModel {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private var id: Long = i

  private var userId: String = null

  private var password: String = null

  @Transient private var rawPassword: String = null
  private var email: String = null

  @Column(nullable = false, columnDefinition = "enum ('facebook', 'twitter', 'google', 'slipp')")
  private var providerId: String = null

  private var providerUserId: String = null

  @Column(nullable = false) private var rank: Int = 0
  private var displayName: String = null

  private var profileUrl: String = null

  private var imageUrl: String = null

  @Column(nullable = false) private var accessToken: String = null
  private var secret: String = null

  private var refreshToken: String = null

  private var expireTime: Long = _

  private var createDate: Date = new Date

  private var blocked: Boolean = false

  private var admined: Boolean = false

  def this() = this(0L)

  def getId: Long = {
    return id
  }

  def setId(id: Long) {
    this.id = id
  }

  def getUserId: String = {
    return userId
  }

  def setUserId(userId: String) {
    this.userId = userId
  }

  def getPassword: String = {
    return password
  }

  def setPassword(password: String) {
    this.password = password
  }

  def getRawPassword: String = {
    return rawPassword
  }

  def setRawPassword(rawPassword: String) {
    this.rawPassword = rawPassword
  }

  def getEmail: String = {
    return email
  }

  def setEmail(email: String) {
    this.email = email
  }

  def getProviderId: String = {
    return providerId
  }

  def getProviderIdBySnsType: ProviderType = {
    return ProviderType.valueOf(providerId)
  }

  def setProviderId(providerId: String) {
    this.providerId = providerId
  }

  def getProviderUserId: String = {
    return providerUserId
  }

  def setProviderUserId(providerUserId: String) {
    this.providerUserId = providerUserId
  }

  def getRank: Int = {
    return rank
  }

  def setRank(rank: Int) {
    this.rank = rank
  }

  def getDisplayName: String = {
    return displayName
  }

  def setDisplayName(displayName: String) {
    this.displayName = displayName
  }

  def getProfileUrl: String = {
    return profileUrl
  }

  def setProfileUrl(profileUrl: String) {
    this.profileUrl = profileUrl
  }

  def getImageUrl: String = {
    return imageUrl
  }

  def setImageUrl(imageUrl: String) {
    this.imageUrl = imageUrl
  }

  def getAccessToken: String = {
    return accessToken
  }

  def setAccessToken(accessToken: String) {
    this.accessToken = accessToken
  }

  def getSecret: String = {
    return secret
  }

  def setSecret(secret: String) {
    this.secret = secret
  }

  def getRefreshToken: String = {
    return refreshToken
  }

  def setRefreshToken(refreshToken: String) {
    this.refreshToken = refreshToken
  }

  def getExpireTime: Long = {
    return expireTime
  }

  def setExpireTime(expireTime: Long) {
    this.expireTime = expireTime
  }

  def getCreateDate: Date = {
    return createDate
  }

  def setCreateDate(createDate: Date) {
    this.createDate = createDate
  }

  def isGuest: Boolean = {
    return false
  }

  def isSameUser(socialUser: SocialUser): Boolean = {
    if (socialUser == null) {
      return false
    }
    return this.id == socialUser.id
  }

  def isFacebookUser: Boolean = {
    return ProviderType.facebook eq getProviderIdBySnsType
  }

  def isSLiPPUser: Boolean = {
    return ProviderType.slipp eq getProviderIdBySnsType
  }

  def isBlocked: Boolean = {
    return blocked
  }

  def block() {
    this.blocked = true
  }

  def isAdmined: Boolean = {
    return admined
  }

  def admin() {
    this.admined = true
  }

  def unadmined() {
    this.admined = false
  }

  def changePassword(encoder: PasswordEncoder, oldPassword: String, newPassword: String) {
    if (!encoder.matches(oldPassword, password)) {
      throw new BadCredentialsException("현재 비밀번호가 다릅니다.")
    }
    this.password = encoder.encode(newPassword)
  }

  def resetPassword(encoder: PasswordEncoder, newPassword: String) {
    this.rawPassword = newPassword
    this.password = encoder.encode(newPassword)
  }

  def update(email: String, userId: String) {
    this.email = email
    this.userId = userId
    if (isSLiPPUser) {
      this.imageUrl = SocialUser.DEFAULT_SLIPP_USER_PROFILE_SUFFIX + MD5Util.md5Hex(email)
    }
  }

  def getUrl: String = {
    s"/users/${id}/${userId}"
  }
}
