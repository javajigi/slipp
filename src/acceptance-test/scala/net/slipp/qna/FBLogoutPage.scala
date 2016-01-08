package net.slipp.qna

import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

class FBLogoutPage(driver: WebDriver) {
  driver.get("http://localhost:8080/users/fblogout")

  def goToLogoutPage = {
    driver.get("http://localhost:8080/users/fblogout")
    new FBLogoutPage(driver)
  }

  def verifyCurrentLoginUser(nickName: String) {
    val loginUserNickName: String = driver.findElement(By.cssSelector("p.nickName")).getText
    assertThat(loginUserNickName, is(nickName))
  }

  def logout = {
    new WebDriverWait(driver, 1000).until(ExpectedConditions.visibilityOfElementLocated(By.id("fbLogoutBtn")))
    driver.findElement(By.id("fbLogoutBtn")).click
    driver.findElement(By.cssSelector("a.link-loginout")).click
    new IndexPage(driver)
  }
}
