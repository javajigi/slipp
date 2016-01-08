package net.slipp.user

import net.slipp.qna.IndexPage
import org.openqa.selenium.{By, WebDriver, WebElement}

class GooglePage(driver: WebDriver) {
  def login(email: String, password: String): IndexPage = {
    driver.findElement(By.id("Email")).clear
    driver.findElement(By.id("Email")).sendKeys(email)
    driver.findElement(By.id("Passwd")).clear
    driver.findElement(By.id("Passwd")).sendKeys(password)
    driver.findElement(By.id("PersistentCookie")).click
    driver.findElement(By.id("signIn")).click
    val approveAccess: WebElement = driver.findElement(By.id("submit_approve_access"))
    if (approveAccess != null) {
      approveAccess.click
    }
    if (isFirstLogin) {
      val loginPage: LoginPage = new LoginPage(driver)
      return loginPage.loginSlipp("박재성")
    }
    return new IndexPage(driver)
  }

  private def isFirstLogin: Boolean = {
    return "로그인 :: SLiPP" == driver.getTitle
  }
}
