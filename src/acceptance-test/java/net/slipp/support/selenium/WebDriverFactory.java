package net.slipp.support.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverFactory {
    public static WebDriver createWebDriver() {
        System.setProperty("webdriver.chrome.driver", "/Users/javajigi/slipp-workspace/chromedriver");
        return new ChromeDriver();
    }
}
