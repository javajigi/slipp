package net.slipp.user

import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class ProfilePage(driver: WebDriver) {
  def verifyPageTitle(nickName: String) {
    assertThat(driver.getTitle, is(nickName + "의 개인공간 :: SLiPP"))
  }

  def goChangePasswordPage: ChangePasswordPage = {
    driver.findElement(By.linkText("비밀번호 변경하기")).click
    return new ChangePasswordPage(driver)
  }
}
