package net.slipp.user;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.slipp.qna.IndexPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage {
	private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);
	
	private WebDriver driver;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		assertThat(driver.getTitle(), is("로그인 :: SLiPP"));
	}

	public FacebookPage loginFacebook() {
		driver.findElement(By.cssSelector("input[value='페이스북 계정으로 로그인']")).click();
		logger.debug("body : {}", driver.getPageSource());
		return new FacebookPage(driver);
	}

	public IndexPage loginSlipp(final String userName) {
		driver.findElement(By.id("userName")).clear();
		driver.findElement(By.id("userName")).sendKeys(userName);
		driver.findElement(By.cssSelector(".btn-success")).click();
		return new IndexPage(driver);
	}
}
