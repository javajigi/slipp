package net.slipp.qna;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Lists;

public class QuestionPage {
    private WebDriver driver;

    public QuestionPage(WebDriver driver) {
        this.driver = driver;
    }

    public QuestionPage(WebDriver driver, String title) {
        this.driver = driver;
        assertThat(driver.getTitle(), is(title));
    }

    public void verify(String title, String contents, String plainTags) {
        WebElement titleElement = driver.findElement(By.cssSelector(".article-title"));
        assertThat(titleElement.getText(), is(title));

        List<String> tags = Arrays.asList(plainTags.split(" "));
        List<WebElement> tagElements = driver.findElements(By.cssSelector("doc > div.tags > ul > li"));
        for (WebElement each : tagElements) {
            String tag = each.findElement(By.cssSelector("a")).getText();
            assertThat(tags.contains(tag), is(true));
        }
    }

    public void answer(String answer) {
        driver.findElement(By.id("contents")).clear();
        driver.findElement(By.id("contents")).sendKeys(answer);
        driver.findElement(By.cssSelector(".btn-submit")).click();
    }

    public void verifyAnswer(String answer) {
        List<String> commentTexts = findCommentTexts();
        assertThat(commentTexts.contains(answer), is(true));
    }

    private List<String> findCommentTexts() {
        List<WebElement> comments = driver.findElements(By.cssSelector("ul.list"));
        List<String> commentTexts = Lists.newArrayList();
        for (WebElement comment : comments) {
            commentTexts.add(comment.findElement(By.xpath("//div[@class='qna-comment']//div[@class='article-doc']/p")).getText());
        }
        return commentTexts;
    }

    public void verifyAnswerCount(String answerCount) {
        String actual = driver.findElement(By.cssSelector("p.count > strong")).getText();
        assertThat(actual, is(answerCount));
    }

    public QuestionPage likeAnswer() {
        driver.findElement(By.cssSelector("a.likeAnswerBtn")).click();
        return new QuestionPage(driver);
    }

    public void verifyLikeCount(int likeCount) {
        String actual = driver.findElement(By.cssSelector("strong.like-count")).getText();
        assertThat(actual, is(likeCount + ""));
    }

    public AnswerUpdateFormPage goToUpdateAnswerPage() {
        driver.findElement(By.cssSelector("a.updateAnswerBtn")).click();
        return new AnswerUpdateFormPage(driver);
    }

    public QuestionFormPage goToUpdatePage() {
        driver.findElement(By.cssSelector(".link-modify-article")).click();
        return new QuestionFormPage(driver);
    }

    public void verifyBestAnswer() {
        String answerBest = driver.findElement(By.cssSelector("span.answer-best")).getText();
        assertThat(answerBest, is("best"));
    }

	public QuestionsPage goToQuestionsPage() {
		driver.findElement(By.linkText("목록으로")).click();
		return new QuestionsPage(driver);
	}
}
