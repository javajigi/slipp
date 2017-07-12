package net.slipp.support

import com.codeborne.selenide.WebDriverRunner
import net.slipp.LoginUser
import net.slipp.qna.IndexPage

class AbstractATTest {
  protected var driver: SharedDriver = null
  protected var environment: SlippEnvironment = null
  protected var indexPage: IndexPage = null

  def setup() {
    this.driver = new SharedDriver
    deleteAllCookies(driver)
    WebDriverRunner.setWebDriver(new SharedDriver)
    this.environment = new SlippEnvironment
  }

  protected def deleteAllCookies(sharedDriver: SharedDriver) {
    sharedDriver.deleteAllCookies
  }

  protected def loginToFacebook(number: Int) = {
    indexPage = new IndexPage(driver)
    val loginUser = getLoginUser(number)
    indexPage = indexPage.loginToFacebook(loginUser)
    loginUser
  }

  private def getLoginUser(number: Int) = {
    val email = environment.getProperty("facebook.email" + number)
    val password = environment.getProperty("facebook.password" + number)
    val nickName = environment.getProperty("facebook.nickName" + number)
    new LoginUser(email, password, nickName)
  }
}
