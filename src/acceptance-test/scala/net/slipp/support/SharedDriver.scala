package net.slipp.support

import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.events.EventFiringWebDriver

object SharedDriver {
  private val REAL_DRIVER: WebDriver = WebDriverFactory.createWebDriver
  private val CLOSE_THREAD: Thread = new Thread() {
    override def run {
      REAL_DRIVER.quit
    }
  }

  Runtime.getRuntime.addShutdownHook(CLOSE_THREAD)
}

class SharedDriver extends EventFiringWebDriver(SharedDriver.REAL_DRIVER) {
  override def close {
    if (Thread.currentThread ne SharedDriver.CLOSE_THREAD) {
      throw new UnsupportedOperationException("You shouldn't close this WebDriver. It's shared and will close when the JVM exits.")
    }
    super.quit
  }

  def deleteAllCookies {
    manage.deleteAllCookies
  }
}
