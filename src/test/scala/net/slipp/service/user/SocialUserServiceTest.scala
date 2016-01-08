package net.slipp.service.user

import java.util.List

import com.google.common.collect.Lists
import net.slipp.domain.user.{ExistedUserException, PasswordDto, SocialUser, SocialUserBuilder}
import net.slipp.repository.user.SocialUserRepository
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.junit.{Before, Ignore, Test}
import org.junit.runner.RunWith
import org.mockito.{InjectMocks, Mock}
import org.mockito.Mockito.{verify, when}
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.social.connect.{Connection, ConnectionRepository, UsersConnectionRepository}
import org.springframework.util.{LinkedMultiValueMap, MultiValueMap}

@RunWith(classOf[MockitoJUnitRunner]) class SocialUserServiceTest {
  @Mock private var usersConnectionRepository: UsersConnectionRepository = null
  @Mock private var connectionRepository: ConnectionRepository = null
  @Mock private var socialUserRepository: SocialUserRepository = null
  private var encoder: BCryptPasswordEncoder = null
  @Mock private var connection: Connection[_] = null
  @InjectMocks private var dut: SocialUserService = new SocialUserService

  @Before def setup {
    encoder = new BCryptPasswordEncoder
    dut.passwordEncoder = encoder
  }

  @Ignore
  @Test
  @throws(classOf[Exception])
  def createNewSocialUser_availableUserId {
    val userId: String = "userId"
    when(usersConnectionRepository.createConnectionRepository(userId)).thenReturn(connectionRepository)
    val connections: MultiValueMap[String, Connection[_]] = new LinkedMultiValueMap[String, Connection[_]]
    when(connectionRepository.findAllConnections).thenReturn(connections)
    dut.createNewSocialUser(userId, connection)
    verify(connectionRepository).addConnection(connection)
  }

  @Ignore
  @Test(expected = classOf[ExistedUserException])
  @throws(classOf[Exception])
  def createNewSocialUser_notAvailableUserId {
    val userId: String = "userId"
    when(usersConnectionRepository.createConnectionRepository(userId)).thenReturn(connectionRepository)
    val connections: MultiValueMap[String, Connection[_]] = new LinkedMultiValueMap[String, Connection[_]]
    connections.add("facebook", connection)
    when(connectionRepository.findAllConnections).thenReturn(connections)
    dut.createNewSocialUser(userId, connection)
  }

  @Test def changePassword {
    val oldPassword: String = "oldPassword"
    val newPassword: String = "newPassword"
    val socialUser: SocialUser = new SocialUserBuilder().withRawPassword(oldPassword).build
    val password: PasswordDto = new PasswordDto(socialUser.getId, oldPassword, newPassword, newPassword)
    when(socialUserRepository.findOne(socialUser.getId)).thenReturn(socialUser)
    val changedUser: SocialUser = dut.changePassword(socialUser, password)
    encoder.matches(newPassword, changedUser.getPassword)
  }

  @Test
  @throws(classOf[Exception])
  def updateSlippUser_isSameEmail {
    val email: String = "javajigi@slipp.net"
    val socialUser: SocialUser = createUser(1L, email, "userId")
    when(socialUserRepository.findOne(socialUser.getId)).thenReturn(socialUser)
    val updateUserId: String = "updateUserId"
    val updateSocialUser: SocialUser = dut.updateSlippUser(socialUser, email, updateUserId)
    assertThat(updateSocialUser.getUserId, is(updateUserId))
  }

  @Test
  @throws(classOf[Exception])
  def updateSlippUser_isNotExistedAnotherEmail {
    val email: String = "javajigi@slipp.net"
    val socialUser: SocialUser = createUser(1L, email, "userId")
    val anotherEmail: String = email + "2"
    when(socialUserRepository.findOne(socialUser.getId)).thenReturn(socialUser)
    when(socialUserRepository.findByEmail(anotherEmail)).thenReturn(null)
    val updateUserId: String = "updateUserId"
    val updateSocialUser: SocialUser = dut.updateSlippUser(socialUser, anotherEmail, updateUserId)
    assertThat(updateSocialUser.getUserId, is(updateUserId))
    verify(socialUserRepository).findByEmail(anotherEmail)
  }

  @Test(expected = classOf[ExistedUserException])
  @throws(classOf[Exception])
  def updateSlippUser_isExistedAnotherEmail {
    val email: String = "javajigi@slipp.net"
    val socialUser: SocialUser = createUser(1L, email, "userId")
    val anotherEmail: String = email + "2"
    when(socialUserRepository.findOne(socialUser.getId)).thenReturn(socialUser)
    val anotherUser: SocialUser = createUser(2L, email + 2, "userId")
    when(socialUserRepository.findByEmail(anotherEmail)).thenReturn(anotherUser)
    val updateUserId: String = "updateUserId"
    dut.updateSlippUser(socialUser, anotherEmail, updateUserId)
  }

  @Test
  @throws(classOf[Exception])
  def updateSlippUser_isNotExistedAnotherUserId {
    val email: String = "javajigi@slipp.net"
    val userId: String = "userId"
    val socialUser: SocialUser = createUser(1L, email, userId)
    val anotherUserId: String = userId + "2"
    when(socialUserRepository.findOne(socialUser.getId)).thenReturn(socialUser)
    when(socialUserRepository.findByEmail(email)).thenReturn(null)
    val users = Lists.newArrayList[SocialUser]
    when(socialUserRepository.findsByUserId(anotherUserId)).thenReturn(users)
    val updateSocialUser: SocialUser = dut.updateSlippUser(socialUser, email, anotherUserId)
    assertThat(updateSocialUser.getUserId, is(anotherUserId))
    verify(socialUserRepository).findByEmail(email)
    verify(socialUserRepository).findsByUserId(anotherUserId)
  }

  @Test(expected = classOf[ExistedUserException])
  @throws(classOf[Exception])
  def updateSlippUser_isExistedAnotherUserId {
    val email: String = "javajigi@slipp.net"
    val userId: String = "userId"
    val socialUser: SocialUser = createUser(1L, email, userId)
    val anotherUserId: String = userId + "2"
    when(socialUserRepository.findOne(socialUser.getId)).thenReturn(socialUser)
    when(socialUserRepository.findByEmail(email)).thenReturn(null)
    val users: List[SocialUser] = Lists.newArrayList(createUser(2L, email, anotherUserId))
    when(socialUserRepository.findsByUserId(anotherUserId)).thenReturn(users)
    dut.updateSlippUser(socialUser, email, anotherUserId)
  }

  private def createUser(id: Long, email: String, nickName: String): SocialUser = {
    val socialUser: SocialUser = new SocialUserBuilder().withId(id).withEmail(email).withUserId(nickName).build
    return socialUser
  }
}
