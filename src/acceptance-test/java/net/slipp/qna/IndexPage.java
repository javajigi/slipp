package net.slipp.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class IndexPage {
	private WebDriver driver;

	public IndexPage(WebDriver driver) {
		this.driver = driver;
		driver.get("http://localhost:8080");
		assertThat(driver.getTitle(), is("SLiPP"));
	}

	public LoginPage goLoginPage() {
		driver.findElement(By.cssSelector(".loginBtn > a")).click();		
		return new LoginPage(driver);
	}
}
