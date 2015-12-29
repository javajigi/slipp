package net.slipp.user

import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import net.slipp.qna.IndexPage
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LoginPage(driver: WebDriver) {
  private val log: Logger = LoggerFactory.getLogger(classOf[LoginPage])

  assertThat(driver.getTitle, is("로그인 :: SLiPP"))

  def loginFacebook: FacebookPage = {
    driver.findElement(By.cssSelector("input[value='페이스북 계정으로 로그인']")).click()
    if (driver.getTitle == "SLiPP") {
    }
    new FacebookPage(driver)
  }

  def loginSlipp(userName: String): IndexPage = {
    driver.findElement(By.id("userId")).clear()
    driver.findElement(By.id("userId")).sendKeys(userName)
    driver.findElement(By.cssSelector(".signin-with-sns-submit-btn")).click()
    new IndexPage(driver)
  }

  def loginToSlipp(email: String, password: String): IndexPage = {
    log.debug(s"email : ${email}, password : ${password}")
    driver.findElement(By.id("authenticationId")).clear()
    driver.findElement(By.id("authenticationId")).sendKeys(email)
    driver.findElement(By.id("authenticationPassword")).clear()
    driver.findElement(By.id("authenticationPassword")).sendKeys(password)
    driver.findElement(By.id("loginSubmitBtn")).click()
    new IndexPage(driver)
  }

  def join(userId: String, email: String): IndexPage = {
    driver.findElement(By.id("email")).clear()
    driver.findElement(By.id("email")).sendKeys(email)
    driver.findElement(By.id("userId")).clear()
    driver.findElement(By.id("userId")).sendKeys(userId)
    driver.findElement(By.cssSelector(".signin-to-slipp-btn")).click()
    new IndexPage(driver)
  }
}
