package net.slipp.web

import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import org.junit.Test

class UserFormTest {
  @Test def create() {
    val userForm = new UserForm
    userForm.setUserId("userId")
    assertThat(userForm.getUserId(), is("userId"))
  }

  @Test def valid_userId() {
    assertUserId("ni", true)
    assertUserId("테스트", true)
    assertUserId("박.재.성", true)
    assertUserId("나만의 닉네임을 가지고", true)
    assertUserId(null, false)
    assertUserId("", false)
    assertUserId("n", false)
    assertUserId("나만의 닉네임을 가지고 테", false)
  }

  def assertUserId(userId: String, expected: Boolean) {
    val email = "email@gmail.com"
    val form = new UserForm(userId, email)
    assertThat(form.isValid(), is(expected))
  }
}