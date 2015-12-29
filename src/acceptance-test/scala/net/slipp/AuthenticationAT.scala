package net.slipp

import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import net.slipp.qna.FBLogoutPage
import net.slipp.qna.IndexPage
import net.slipp.service.user.{PasswordGenerator, FixedPasswordGenerator}
import net.slipp.support.AbstractATTest
import net.slipp.user.ChangePasswordPage
import net.slipp.user.LoginPage
import net.slipp.user.ProfilePage
import net.slipp.web.UserForm
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.openqa.selenium.Alert

class AuthenticationAT extends AbstractATTest {
  private var index: IndexPage = null

  @Before override def setup {
    super.setup
    driver.get("http://localhost:8080")
    index = new IndexPage(driver)
  }

  @Test
  @throws(classOf[Exception])
  def login_after_relogin {
    val firstNickName: String = environment.getProperty("facebook.nickName1")
    index.loginToFacebook(environment.getProperty("facebook.email1"), environment.getProperty("facebook.password1"), firstNickName)
    val logoutPage: FBLogoutPage = new FBLogoutPage(driver)
    logoutPage.verifyCurrentLoginUser(firstNickName)
    logoutPage.logout
    val secondNickName: String = environment.getProperty("facebook.nickName2")
    index.loginToFacebook(environment.getProperty("facebook.email2"), environment.getProperty("facebook.password2"), secondNickName)
    logoutPage.goToLogoutPage
    logoutPage.verifyCurrentLoginUser(secondNickName)
  }

  @Test
  @throws(classOf[Exception])
  def join {
    join_to_slipp
  }

  @throws(classOf[Exception])
  private def join_to_slipp: UserForm = {
    val userId: String = createUserId
    val email: String = System.currentTimeMillis + environment.getProperty("slipp.email1")
    val loginPage: LoginPage = index.goLoginPage
    index = loginPage.join(userId, email)
    assertThat(index.isLoginStatus, is(true))
    return new UserForm(userId, email)
  }

  private def createUserId: String = {
    return environment.getProperty("slipp.userId1") + System.currentTimeMillis
  }

  @Test
  @throws(classOf[Exception])
  def join_relogin {
    val userForm: UserForm = join_to_slipp
    index = index.logout
    val loginPage: LoginPage = index.goLoginPage
    loginPage.loginToSlipp(userForm.getEmail, PasswordGenerator.DEFAULT_FIXED_PASSWORD)
    assertThat(index.isLoginStatus, is(true))
  }

  @Test
  @throws(classOf[Exception])
  def join_change_password_login {
    val userForm: UserForm = join_to_slipp
    val profilePage: ProfilePage = index.goProfilePage
    profilePage.verifyPageTitle(userForm.getUserId)
    val changePasswordPage: ChangePasswordPage = profilePage.goChangePasswordPage
    val oldPassword: String = PasswordGenerator.DEFAULT_FIXED_PASSWORD
    val newPassword: String = "newPassword"
    changePasswordPage.changePassword(oldPassword, newPassword)
    val alert: Alert = driver.switchTo.alert
    alert.accept
    val loginPage: LoginPage = index.goLoginPage
    loginPage.loginToSlipp(userForm.getEmail, newPassword)
    assertThat(index.isLoginStatus, is(true))
  }

  @After def teardown() {
    val logoutPage: FBLogoutPage = new FBLogoutPage(driver)
    logoutPage.logout
  }
}
