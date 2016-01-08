package net.slipp.domain.user

import net.slipp.support.test.UnitTest
import org.hamcrest.CoreMatchers.is
import org.junit.Assert._
import org.junit.{Before, Test}
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class SocialUserTest extends UnitTest {
  private var dut: SocialUser = null
  private var encoder: BCryptPasswordEncoder = null

  @Before def setup {
    dut = new SocialUser(10L)
    encoder = new BCryptPasswordEncoder
  }

  @Test def isSameUser_null {
    assertThat(dut.isSameUser(null), is(false))
  }

  @Test def isSameUser_guestUser {
    assertThat(dut.isSameUser(SocialUser.GUEST_USER), is(false))
  }

  @Test def isSameUser_sameUser {
    val newUser: SocialUser = new SocialUser(10L)
    assertThat(dut.isSameUser(newUser), is(true))
  }

  @Test def isSameUser_differentUser {
    val newUser: SocialUser = new SocialUser(11L)
    assertThat(dut.isSameUser(newUser), is(false))
  }

  @Test def changePassword_이전_비밀번호가_같다 {
    val oldPassword: String = "oldPassword"
    val newPassword: String = "newPassword"
    val socialUser = aSomeUser(password = encoder.encode(oldPassword))
    socialUser.changePassword(encoder, oldPassword, newPassword)
    assertTrue(encoder.matches(newPassword, socialUser.getPassword))
  }

  @Test(expected = classOf[BadCredentialsException])
  def changePassword_이전_비밀번호가_다르다 {
    val oldPassword: String = "oldPassword"
    val newPassword: String = "newPassword"
    val socialUser: SocialUser = new SocialUserBuilder().withRawPassword(oldPassword).build
    socialUser.changePassword(encoder, oldPassword + "2", newPassword)
    assertThat(socialUser.getPassword, is(encoder.encode(newPassword)))
  }
}
