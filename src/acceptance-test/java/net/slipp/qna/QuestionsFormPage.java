package net.slipp.qna;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class QuestionsFormPage {
	private WebDriver driver;

	public QuestionsFormPage(WebDriver driver) {
		this.driver = driver;
	}

	public QuestionPage question(String title, String contents, String tags) {
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title")).sendKeys(title);
		driver.findElement(By.id("contents")).clear();
		driver.findElement(By.id("contents")).sendKeys(contents);
		driver.findElement(By.id("plainTags")).clear();
		driver.findElement(By.id("plainTags")).sendKeys(tags);
		driver.findElement(By.id("confirmBtn")).click();
		return new QuestionPage(driver, title);
	}
}
