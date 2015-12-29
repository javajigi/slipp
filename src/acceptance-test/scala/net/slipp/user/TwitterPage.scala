package net.slipp.user

import net.slipp.qna.IndexPage
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class TwitterPage(driver: WebDriver) {
  def login(username: String, password: String): IndexPage = {
    driver.findElement(By.id("username_or_email")).clear
    driver.findElement(By.id("username_or_email")).sendKeys(username)
    driver.findElement(By.id("password")).clear
    driver.findElement(By.id("password")).sendKeys(password)
    driver.findElement(By.id("remember")).click
    driver.findElement(By.id("allow")).click
    if (isFirstLogin) {
      val loginPage: LoginPage = new LoginPage(driver)
      return loginPage.loginSlipp("javajigi")
    }
    return new IndexPage(driver)
  }

  private def isFirstLogin: Boolean = {
    return "로그인 :: SLiPP" == driver.getTitle
  }
}
