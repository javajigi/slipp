package net.slipp.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AdminTagPage {
	private WebDriver driver;

	public AdminTagPage(WebDriver driver) {
		this.driver = driver;
		assertThat(driver.getTitle(), is("태그관리 :: SLiPP"));
		
		WebElement tagTab = driver.findElement(By.cssSelector("#tagManagement"));
		assertThat(tagTab.getAttribute("class"), is("active"));
	}

}
