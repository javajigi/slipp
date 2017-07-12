package net.slipp

import com.codeborne.selenide.Selenide._
import net.slipp.support.AbstractATTest
import org.junit.{Before, Test}
import org.openqa.selenium.By

class SelAuthenticationAT extends AbstractATTest {
  @Before override def setup(): Unit = {
    super.setup()
  }

  @Test def search(): Unit = {
    open("http://google.com")
    $(By.name("q")).`val`("selenide").pressEnter()
  }
}