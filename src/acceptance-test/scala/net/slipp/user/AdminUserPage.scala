package net.slipp.user

import org.hamcrest.CoreMatchers.is
import org.junit.Assert._

import org.openqa.selenium.WebDriver

class AdminUserPage(driver: WebDriver) {
  assertThat(driver.getTitle, is("회원관리 :: SLiPP"))

}
