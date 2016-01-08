package net.slipp.service.user

import java.util.UUID
import javax.annotation.Resource

import net.slipp.domain.ProviderType
import net.slipp.domain.user.{ExistedUserException, PasswordDto, SocialUser}
import net.slipp.repository.user.SocialUserRepository
import net.slipp.service.MailService
import net.slipp.support.utils.MD5Util
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.{Page, Pageable}
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.social.connect.{Connection, ConnectionKey, ConnectionRepository, UsersConnectionRepository}
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.Assert

@Service
@Transactional class SocialUserService {
  @Resource(name = "usersConnectionRepository") private var usersConnectionRepository: UsersConnectionRepository = null
  @Resource(name = "socialUserRepository") private var socialUserRepository: SocialUserRepository = null
  @Resource(name = "passwordEncoder") private[user] var passwordEncoder: PasswordEncoder = null
  @Autowired private var passwordGenerator: PasswordGenerator = null
  @Resource(name = "mailService") private var mailService: MailService = null

  @throws(classOf[ExistedUserException])
  def createNewSocialUser(userId: String, connection: Connection[_]) {
    Assert.notNull(userId, "userId can't be null!")
    Assert.notNull(connection, "connection can't be null!")
    val connectionRepository: ConnectionRepository = usersConnectionRepository.createConnectionRepository(userId)
    if (!isUserIdAvailable(userId)) {
      throw new ExistedUserException(userId + " already is existed user!")
    }
    connectionRepository.addConnection(connection)
  }

  private def isUserIdAvailable(userId: String): Boolean = {
    val socialUsers = socialUserRepository.findsByUserId(userId)
    return socialUsers.isEmpty
  }

  def findsUser(pageable: Pageable): Page[SocialUser] = {
    return socialUserRepository.findAll(pageable)
  }

  def findsUser(searchTerm: String, pageable: Pageable): Page[SocialUser] = {
    if (StringUtils.isBlank(searchTerm)) {
      return socialUserRepository.findAll(pageable)
    }
    return socialUserRepository.findsBySearch(searchTerm, pageable)
  }

  def findById(id: Long): SocialUser = {
    Assert.notNull(id, "id can't be null!")
    return socialUserRepository.findOne(id)
  }

  def findByUserId(userId: String): SocialUser = {
    Assert.notNull(userId, "userId can't be null!")
    val socialUsers = socialUserRepository.findsByUserId(userId)
    if (socialUsers.isEmpty) {
      return null
    }
    return socialUsers.get(0)
  }

  def findAdminUser: SocialUser = {
    return findByUserId("자바지기")
  }

  def findByEmail(email: String): SocialUser = {
    Assert.notNull(email, "email can't be null!")
    return socialUserRepository.findByEmail(email)
  }

  def findByUserIdAndConnectionKey(userId: String, connectionKey: ConnectionKey): SocialUser = {
    Assert.notNull(userId, "userId can't be null!")
    Assert.notNull(connectionKey, "connectionKey can't be null!")
    val socialUser: SocialUser = socialUserRepository.findByUserIdAndProviderIdAndProviderUserId(userId, connectionKey.getProviderId, connectionKey.getProviderUserId)
    return socialUser
  }

  def createSlippUser(userId: String, email: String): SocialUser = {
    var existedUser: SocialUser = findByUserId(userId)
    if (existedUser != null) {
      throw new IllegalArgumentException(userId + " userId already is existed User.")
    }
    existedUser = findByEmail(email)
    if (existedUser != null) {
      throw new IllegalArgumentException(email + " email address already is existed User.")
    }
    val rawPassword: String = passwordGenerator.generate
    val uuid: String = UUID.randomUUID.toString
    val socialUser: SocialUser = new SocialUser
    socialUser.setUserId(userId)
    socialUser.setEmail(email)
    socialUser.setImageUrl(SocialUser.DEFAULT_SLIPP_USER_PROFILE_SUFFIX + MD5Util.md5Hex(email))
    socialUser.setProviderId(ProviderType.slipp.name)
    socialUser.setProviderUserId(userId)
    socialUser.setRank(1)
    socialUser.setAccessToken(uuid)
    socialUser.setRawPassword(rawPassword)
    socialUser.setPassword(encodePassword(rawPassword))
    socialUserRepository.save(socialUser)
    mailService.sendPasswordInformation(socialUser)
    return socialUser
  }

  @throws(classOf[ExistedUserException])
  def updateSlippUser(loginUser: SocialUser, email: String, userId: String): SocialUser = {
    Assert.notNull(loginUser, "loginUser can't be null!")
    Assert.notNull(email, "email can't be null!")
    Assert.notNull(userId, "userId can't be null!")
    val socialUser: SocialUser = socialUserRepository.findOne(loginUser.getId)
    val socialUserByEmail: SocialUser = socialUserRepository.findByEmail(email)
    if (socialUserByEmail != null && !socialUser.isSameUser(socialUserByEmail)) {
      throw new ExistedUserException(String.format("%s 이메일은 다른 사용자가 이미 사용하고 있는 이메일입니다.", email))
    }
    val socialUserByUserId: SocialUser = findByUserId(userId)
    if (socialUserByUserId != null && !socialUser.isSameUser(socialUserByUserId)) {
      throw new ExistedUserException(String.format("%s 닉네임은 다른 사용자가 이미 사용하고 있는 닉네임입니다.", userId))
    }
    socialUser.update(email, userId)
    return socialUser
  }

  private def encodePassword(rawPass: String): String = {
    return passwordEncoder.encode(rawPass)
  }

  def changePassword(loginUser: SocialUser, password: PasswordDto): SocialUser = {
    val user: SocialUser = socialUserRepository.findOne(password.getId)
    if (user == null) {
      return null
    }
    user.changePassword(passwordEncoder, password.getOldPassword, password.getNewPassword)
    return user
  }

  def resetPassword(socialUser: SocialUser): SocialUser = {
    val rawPassword: String = passwordGenerator.generate
    socialUser.resetPassword(passwordEncoder, rawPassword)
    mailService.sendPasswordInformation(socialUser)
    return socialUser
  }
}
