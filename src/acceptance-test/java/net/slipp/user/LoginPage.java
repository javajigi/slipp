package net.slipp.user;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import net.slipp.qna.IndexPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
	private WebDriver driver;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		assertThat(driver.getTitle(), is("로그인 :: SLiPP"));
	}

	public FacebookPage loginFacebook() {
		driver.findElement(By.cssSelector("input[value='페이스북 계정으로 로그인']")).click();
		return new FacebookPage(driver);
	}

	public IndexPage loginSlipp(final String userName) {
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(userName);
		driver.findElement(By.cssSelector(".btn-success")).click();
		return new IndexPage(driver);
	}
}
