package net.slipp.support

import net.slipp.LoginUser
import net.slipp.qna.IndexPage

class AbstractATTest {
  protected var driver: SharedDriver = null
  protected var environment: SlippEnvironment = null
  protected var indexPage: IndexPage = null

  def setup() {
    this.driver = new SharedDriver
    deleteAllCookies(driver)
    this.environment = new SlippEnvironment
  }

  protected def deleteAllCookies(sharedDriver: SharedDriver) {
    sharedDriver.deleteAllCookies
  }

  protected def loginToFacebook(number: Int): LoginUser = {
    indexPage = new IndexPage(driver)
    val loginUser: LoginUser = getLoginUser(number)
    indexPage = indexPage.loginToFacebook(loginUser)
    return loginUser
  }

  private def getLoginUser(number: Int): LoginUser = {
    val email: String = environment.getProperty("facebook.email" + number)
    val password: String = environment.getProperty("facebook.password" + number)
    val nickName: String = environment.getProperty("facebook.nickName" + number)
    return new LoginUser(email, password, nickName)
  }
}
