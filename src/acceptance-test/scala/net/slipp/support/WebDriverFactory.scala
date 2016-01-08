package net.slipp.support

import net.slipp.support.WebDriverType.WebDriverType
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.ie.InternetExplorerDriver


object WebDriverType extends Enumeration {
  type WebDriverType = Value
  val IE, FF, CHROME = Value
}

object WebDriverFactory {
  private val DEFAULT_BROWSER_TYPE: WebDriverType = WebDriverType.CHROME

  def createWebDriver: WebDriver = {
    var driver: WebDriver = null
    if (DEFAULT_BROWSER_TYPE eq WebDriverType.CHROME) {
      System.setProperty("webdriver.chrome.driver", ChromWebDriverUtils.getChromeWebDriverPath)
      driver = new ChromeDriver
    }
    if (DEFAULT_BROWSER_TYPE eq WebDriverType.FF) {
      driver = new FirefoxDriver
    }
    if (DEFAULT_BROWSER_TYPE eq WebDriverType.IE) {
      driver = new InternetExplorerDriver
    }
    driver.manage.window.maximize
    return driver
  }

}

