package net.slipp.social.security

import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import org.junit.Test

class SlippUserDetailsServiceTest {
  @Test def isAdmin {
    val adminUsers: String = "javajigi:sanjigi"
    val dut: SlippUserDetailsService = new SlippUserDetailsService
    dut.setAdminUsers(adminUsers)
    assertThat(dut.isRootAdmin("javajigi"), is(true))
    assertThat(dut.isRootAdmin("userId"), is(false))
  }
}
