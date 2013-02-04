package net.slipp.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Lists;

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
	
	public boolean existNewTag(String newTag) {
		List<String> newTags = findAllNewTags();
		return newTags.contains(newTag);
	}

	private List<String> findAllNewTags() {
		List<String> newTags = Lists.newArrayList();
		List<WebElement> tagElements = driver.findElements(By.xpath("//tbody/tr[@class='newTags']/td[1]"));
		for (WebElement tagElement : tagElements) {
			newTags.add(tagElement.getText());
		}
		return newTags;
	}

	public boolean hasDuplidateErrorMessage() {
		try {
			driver.findElement(By.cssSelector("label.error"));
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public AdminTagPage moveToPoolTag() {
		driver.findElement(By.id("moveToPoolTagBtn")).click();
		return this;
	}
}
