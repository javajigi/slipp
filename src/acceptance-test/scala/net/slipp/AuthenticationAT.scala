package net.slipp

import net.slipp.qna.{FBLogoutPage, IndexPage}
import net.slipp.service.user.PasswordGenerator
import net.slipp.support.AbstractATTest
import net.slipp.web.UserForm
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.junit.{After, Before, Test}

class AuthenticationAT extends AbstractATTest {
  private var index: IndexPage = null

  @Before override def setup() {
    super.setup()
    driver.get("http://localhost:8080")
    index = new IndexPage(driver)
  }

  @Test def login_after_relogin() {
    val firstNickName = environment.getProperty("facebook.nickName1")
    index.loginToFacebook(environment.getProperty("facebook.email1"), environment.getProperty("facebook.password1"), firstNickName)
    val logoutPage = new FBLogoutPage(driver)
    logoutPage.verifyCurrentLoginUser(firstNickName)
    logoutPage.logout
    val secondNickName = environment.getProperty("facebook.nickName2")
    index.loginToFacebook(environment.getProperty("facebook.email2"), environment.getProperty("facebook.password2"), secondNickName)
    logoutPage.goToLogoutPage
    logoutPage.verifyCurrentLoginUser(secondNickName)
  }

  @Test def login_admin() {
    val indexPage = index.loginToFacebook(environment.getProperty("facebook.email1"), environment.getProperty("facebook.password1"), environment.getProperty("facebook.nickName1"))
    indexPage.verifyAdminPage()
    val logoutPage = new FBLogoutPage(driver)
    logoutPage.logout
    index.loginToFacebook(environment.getProperty("facebook.email2"), environment.getProperty("facebook.password2"), environment.getProperty("facebook.nickName2"))
    indexPage.verifyNonAdminPage()
  }

  @Test def join() {
    join_to_slipp
  }

  private def join_to_slipp = {
    val userId = createUserId
    val email = System.currentTimeMillis + environment.getProperty("slipp.email1")
    val loginPage = index.goLoginPage
    index = loginPage.join(userId, email)
    assertThat(index.isLoginStatus, is(true))
    new UserForm(userId, email)
  }

  private def createUserId = {
    environment.getProperty("slipp.userId1") + System.currentTimeMillis
  }

  @Test def join_relogin() {
    val userForm = join_to_slipp
    index = index.logout
    val loginPage = index.goLoginPage
    loginPage.loginToSlipp(userForm.getEmail, PasswordGenerator.DEFAULT_FIXED_PASSWORD)
    assertThat(index.isLoginStatus, is(true))
  }

  @Test def join_change_password_login() {
    val userForm = join_to_slipp
    val profilePage = index.goProfilePage
    profilePage.verifyPageTitle(userForm.getUserId)

    val changePasswordPage = profilePage.goChangePasswordPage
    val oldPassword = PasswordGenerator.DEFAULT_FIXED_PASSWORD
    val newPassword = "newPassword"
    changePasswordPage.changePassword(oldPassword, newPassword)

    val alert = driver.switchTo.alert
    alert.accept()

    val loginPage = index.goLoginPage
    loginPage.loginToSlipp(userForm.getEmail, newPassword)
    assertThat(index.isLoginStatus, is(true))
  }

  @After def teardown() {
    new FBLogoutPage(driver).logout
  }
}
