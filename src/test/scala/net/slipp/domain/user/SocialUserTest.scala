package net.slipp.domain.user

import org.junit.Before
import org.junit.Test
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat

class SocialUserTest {
  private var dut: SocialUser = null
  private var encoder: BCryptPasswordEncoder = null

  @Before def setup {
    dut = new SocialUser(10)
    encoder = new BCryptPasswordEncoder
  }

  @Test def isSameUser_null {
    assertThat(dut.isSameUser(null), is(false))
  }

  @Test def isSameUser_guestUser {
    assertThat(dut.isSameUser(SocialUser.GUEST_USER), is(false))
  }

  @Test def isSameUser_sameUser {
    val newUser: SocialUser = new SocialUser(10)
    assertThat(dut.isSameUser(newUser), is(true))
  }

  @Test def isSameUser_differentUser {
    val newUser: SocialUser = new SocialUser(11)
    assertThat(dut.isSameUser(newUser), is(false))
  }

  @Test def changePassword_이전_비밀번호가_같다 {
    val oldPassword: String = "oldPassword"
    val newPassword: String = "newPassword"
    val socialUser: SocialUser = new SocialUserBuilder().withRawPassword(oldPassword).build
    socialUser.changePassword(encoder, oldPassword, newPassword)
    assertThat(socialUser.getPassword, is(encoder.encode(newPassword)))
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
