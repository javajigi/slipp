package net.slipp.web.user

import net.slipp.domain.user.SocialUser

import org.junit.Before
import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers._

class ApiUsersControllerTest {
  private var dut: ApiUsersController = null
  
  @Before 
  def setup() {
    dut = new ApiUsersController()
  }
  
  @Test
  def checkDuplicate_doesnot_existed() {
    val actual = dut.checkDuplicate(SocialUser.GUEST_USER, null)
    assertThat(actual, is(false))
  }
  
  @Test
  def checkDuplicate_login_isSameUser() {
    val loginUser = new SocialUser(1L)
    val existedUser = loginUser
    val actual = dut.checkDuplicate(loginUser, existedUser)
    assertThat(actual, is(false))
  }
  
  @Test
  def checkDuplicate_login_isNotSameUser() {
    val loginUser = new SocialUser(1L)
    val existedUser = new SocialUser(2L)
    val actual = dut.checkDuplicate(loginUser, existedUser)
    assertThat(actual, is(true))
  }
}