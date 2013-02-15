package net.slipp.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class WebDriverFactory {
	private static final WebDriverType DEFAULT_BROWSER_TYPE = WebDriverType.CHROME;

	public static WebDriver createWebDriver() {
    	WebDriver driver = null;
    	if (DEFAULT_BROWSER_TYPE == WebDriverType.CHROME) {
    		System.setProperty("webdriver.chrome.driver", ChromWebDriverUtils.getChromeWebDriverPath());
    		driver = new ChromeDriver();
    	}
    	
    	if (DEFAULT_BROWSER_TYPE == WebDriverType.FF){
    		driver = new FirefoxDriver();
    	}
    	
    	if (DEFAULT_BROWSER_TYPE == WebDriverType.IE){
    		driver = new InternetExplorerDriver();
    	}
    	
    	driver.manage().window().maximize();
        return driver;
    }

	private enum WebDriverType {
		IE, FF, CHROME;
	}
}
