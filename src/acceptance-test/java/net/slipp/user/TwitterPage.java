package net.slipp.user;

import net.slipp.qna.IndexPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TwitterPage {
	private WebDriver driver;

	public TwitterPage(WebDriver driver) {
		this.driver = driver;
	}

	public IndexPage login(String username, String password) {
		driver.findElement(By.id("username_or_email")).clear();
		driver.findElement(By.id("username_or_email")).sendKeys(username);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("remember")).click();
		driver.findElement(By.id("allow")).click();
		
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
