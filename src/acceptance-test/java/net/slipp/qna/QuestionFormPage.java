package net.slipp.qna;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class QuestionFormPage {
	private WebDriver driver;

	public QuestionFormPage(WebDriver driver) {
		this.driver = driver;
	}

	public QuestionPage question(QuestionFixture questionFixture) {
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title")).sendKeys(questionFixture.getTitle());
		driver.findElement(By.id("contents")).clear();
		driver.findElement(By.id("contents")).sendKeys(questionFixture.getContents());
		driver.findElement(By.id("plainTags")).clear();
		driver.findElement(By.id("plainTags")).sendKeys(questionFixture.getPlainTags());
		driver.findElement(By.cssSelector(".btn-submit")).click();
		return new QuestionPage(driver, questionFixture.getTitle());
	}
}
