package net.slipp.qna;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class QuestionsPage {
	private WebDriver driver;

	public QuestionsPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void verifyFirstTitle(String title) {
		WebElement firstQuestion = findQuestion(0);
		String actual = firstQuestion.findElement(By.cssSelector("strong.subject > a")).getText();
		assertThat(actual, is(title));
	}
	
	public void verifyFirstNickName(String nickName) {
		WebElement firstQuestion = findQuestion(0);
		String actual = firstQuestion.findElement(By.cssSelector("div.auth-info > a.author")).getText();
		assertThat(actual, is(nickName));
	}
	
	public void verifyFirstAnswerCount(int answerCount) {
		WebElement firstQuestion = findQuestion(0);
		String actual = firstQuestion.findElement(By.cssSelector("div.reply > span.point")).getText();
		assertThat(actual, is(answerCount + ""));		
	}
	
	private WebElement findQuestion(int index) {
		return findQuestions().get(index);
	}
	
	private List<WebElement> findQuestions() {
		return driver.findElements(By.cssSelector("ul.list > li"));
	}
}
