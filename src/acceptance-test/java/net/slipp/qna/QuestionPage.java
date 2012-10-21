package net.slipp.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Lists;

public class QuestionPage {
	private WebDriver driver;

	public QuestionPage(WebDriver driver, String title) {
		this.driver = driver;
		assertThat(driver.getTitle(), is(title));
	}

    public void verify(String title, String contents, String plainTags) {
        WebElement titleElement = driver.findElement(By.cssSelector("strong.subject"));
        assertThat(titleElement.getText(), is(title));
        
        List<String> tags = Arrays.asList(plainTags.split(" "));
        List<WebElement> tagElements = driver.findElements(By.cssSelector("p.tags"));
        for (WebElement each : tagElements) {
			String tag = each.findElement(By.cssSelector("a")).getText();
			assertThat(tags.contains(tag), is(true));
		}
    }

	public NewTagsPage goNewTagsPage() {
		driver.findElement(By.cssSelector("#newTagManagement > a")).click();
		return new NewTagsPage(driver);
	}

	public void answer(String answer) {
		driver.findElement(By.cssSelector("div.forum > div.list > a")).click();
		driver.findElement(By.id("contents")).clear();
		driver.findElement(By.id("contents")).sendKeys(answer);
		driver.findElement(By.id("answerBtn")).click();
	}

	public void verifyAnswer(String answer) {
		List<String> commentTexts = findCommentTexts();
		assertThat(commentTexts.contains(answer), is(true));
	}

	private List<String> findCommentTexts() {
		List<WebElement> comments = driver.findElements(By.cssSelector("div.commentList"));
		List<String> commentTexts = Lists.newArrayList();
		for (WebElement comment : comments) {
			commentTexts.add(comment.findElement(By.cssSelector("div.list > div.cont > p")).getText());
		}
		return commentTexts;
	}

	public void verifyAnswerCount(String answerCount) {
		String actual = driver.findElement(By.cssSelector("span.answerNum > strong")).getText();
		assertThat(actual, is(answerCount));
	}
}
