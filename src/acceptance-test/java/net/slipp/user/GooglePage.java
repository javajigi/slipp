package net.slipp.user;

import net.slipp.qna.IndexPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GooglePage {
	private WebDriver driver;

	public GooglePage(WebDriver driver) {
		this.driver = driver;
	}

	public IndexPage login(String email, String password) {
		driver.findElement(By.id("Email")).clear();
		driver.findElement(By.id("Email")).sendKeys(email);
		driver.findElement(By.id("Passwd")).clear();
		driver.findElement(By.id("Passwd")).sendKeys(password);
		driver.findElement(By.id("PersistentCookie")).click();
		driver.findElement(By.id("signIn")).click();
		
		WebElement approveAccess = driver.findElement(By.id("submit_approve_access"));
		if (approveAccess != null) {
			approveAccess.click();
		}
		
		if (isFirstLogin()) {
			LoginPage loginPage = new LoginPage(driver);
			return loginPage.loginSlipp("박재성");
		}
		
		return new IndexPage(driver);
	}
	
	private boolean isFirstLogin() {
		return "로그인 :: SLiPP".equals(driver.getTitle());
	}
}
