package net.slipp.user;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import net.slipp.qna.IndexPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacebookPage {
	private static final Logger logger = LoggerFactory.getLogger(FacebookPage.class);
	
	private WebDriver driver;
	
	public FacebookPage(WebDriver driver) {
		this.driver = driver;
		assertThat(driver.getTitle(), is("Log In | Facebook"));
	}
	
	public IndexPage login(String email, String password) {
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys(email);
		driver.findElement(By.id("pass")).clear();
		driver.findElement(By.id("pass")).sendKeys(password);
		driver.findElement(By.id("persist_box")).click();
		driver.findElement(By.cssSelector("#loginbutton > input")).click();
		logger.debug("body : {}", driver.getPageSource());
		return new IndexPage(driver);
	}
}
