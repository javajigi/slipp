package net.slipp.user;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.slipp.qna.IndexPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FacebookPage {
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
		
		if (isFirstLogin()) {
			LoginPage loginPage = new LoginPage(driver);
			return loginPage.loginSlipp("javajigi");
		}
		
		return new IndexPage(driver);
	}

	private boolean isFirstLogin() {
		return "로그인 :: SLiPP".equals(driver.getTitle());
	}
}
