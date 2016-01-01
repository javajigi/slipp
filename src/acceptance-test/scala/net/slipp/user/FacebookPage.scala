package net.slipp.user

import net.slipp.LoginUser
import net.slipp.qna.IndexPage
import org.openqa.selenium.{By, WebDriver}

class FacebookPage(driver: WebDriver) {
  def login(loginUser: LoginUser): IndexPage = {
    driver.findElement(By.id("email")).clear
    driver.findElement(By.id("email")).sendKeys(loginUser.email)
    driver.findElement(By.id("pass")).clear
    driver.findElement(By.id("pass")).sendKeys(loginUser.password)
    driver.findElement(By.id("persist_box")).click
    driver.findElement(By.cssSelector("#loginbutton > input")).click

    if (isFirstLogin) {
      val loginPage: LoginPage = new LoginPage(driver)
      loginPage.loginSlipp(loginUser.nickName)
    } else {
      if (driver.findElements(By.cssSelector(".layerConfirm")).size != 0) {
        driver.findElement(By.cssSelector(".layerConfirm")).click
      }
      new IndexPage(driver)
    }
  }

  private def isFirstLogin = {
    "로그인 :: SLiPP" == driver.getTitle
  }
}
