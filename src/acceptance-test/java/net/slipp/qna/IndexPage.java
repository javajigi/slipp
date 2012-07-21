package net.slipp.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import net.slipp.user.LoginPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class IndexPage {
	private WebDriver driver;

	public IndexPage(WebDriver driver) {
		this.driver = driver;
		assertThat(driver.getTitle(), is("SLiPP"));
	}

	public LoginPage goLoginPage() {
		driver.findElement(By.cssSelector(".loginBtn > a")).click();		
		return new LoginPage(driver);
	}
	
	public boolean logout() {
		WebElement webElement = driver.findElement(By.cssSelector(".loginBtn > a"));
		if (webElement == null) {
			return false;
		} else {
			return true;
		}
	}

	public AdminTagPage goAdminTagPage() {
		driver.findElement(By.cssSelector(".tagManagement > a")).click();
		return new AdminTagPage(driver);
	}
}
