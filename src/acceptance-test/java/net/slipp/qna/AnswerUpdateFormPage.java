package net.slipp.qna;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AnswerUpdateFormPage {
	private WebDriver driver;

	public AnswerUpdateFormPage(WebDriver driver) {
		this.driver = driver;
	}

	public QuestionPage updateAnswer(String answer) {
        driver.findElement(By.id("contents")).clear();
        driver.findElement(By.id("contents")).sendKeys(answer);
        driver.findElement(By.cssSelector(".btn-submit")).click();
		return new QuestionPage(driver);
	}
}
