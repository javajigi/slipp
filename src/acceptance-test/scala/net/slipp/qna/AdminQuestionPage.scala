package net.slipp.qna

import net.slipp.user.AdminUserPage
import org.hamcrest.CoreMatchers.is
import org.junit.Assert._

import org.openqa.selenium.{By, WebDriver}

class AdminQuestionPage(driver: WebDriver) {
  assertThat(driver.getTitle, is("질문 관리 :: SLiPP"))

  def goAdminUserPage() = {
    driver.findElement(By.cssSelector("#userManagement")).click()
    new AdminUserPage(driver)
  }

  def goAdminTagPage() = {
    driver.findElement(By.cssSelector("#tagManagement")).click()
    new AdminTagPage(driver)
  }

  def goAdminQuestionPage() = {
    driver.findElement(By.cssSelector("#questionManagement")).click()
    new AdminQuestionPage(driver)
  }

}
