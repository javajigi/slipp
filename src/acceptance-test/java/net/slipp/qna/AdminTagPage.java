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

	public AdminTagPage createTag(String newTag) {
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(newTag);
		driver.findElement(By.cssSelector("#tagForm > button")).click();
		return new AdminTagPage(driver);
	}

	public AdminTagPage validateNewTag(String newTag) {
		WebElement tags = driver.findElement(By.xpath("//tbody/tr[1]/td[2]"));
		assertThat(tags.getText(), is(newTag));
		return this;
	}
}
