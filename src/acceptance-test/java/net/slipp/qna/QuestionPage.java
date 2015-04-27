package net.slipp.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class QuestionPage {
	private static final Logger log = LoggerFactory.getLogger(QuestionPage.class);
	
    private WebDriver driver;

    public QuestionPage(WebDriver driver) {
        this.driver = driver;
    }

    public QuestionPage(WebDriver driver, String title) {
        this.driver = driver;
        assertThat(driver.getTitle(), is(title));
    }

    public void verify(String title, String contents, String plainTags) {
        WebElement titleElement = driver.findElement(By.cssSelector(".qna-title"));
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
        log.debug("comments size : {}", commentTexts.size());
//        assertThat(commentTexts.contains(answer), is(true));
    }

    private List<String> findCommentTexts() {
        List<WebElement> comments = driver.findElements(By.cssSelector("div.comment-doc"));
        List<String> commentTexts = Lists.newArrayList();
        for (WebElement comment : comments) {
            commentTexts.add(comment.findElement(By.cssSelector("p")).getText());
        }
        return commentTexts;
    }

    public void verifyAnswerCount(String answerCount) {
        String actual = driver.findElement(By.cssSelector(".qna-comment-count > strong")).getText();
        assertThat(actual, is(answerCount));
    }

    public QuestionPage likeAnswer() {
        driver.findElement(By.cssSelector("button.btn-like-article > strong.like-count")).click();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        return new QuestionPage(driver);
    }

    public void verifyLikeCount(int likeCount) {
        String actual = driver.findElement(By.cssSelector("button.btn-like-article > strong.like-count")).getText();
        assertThat(actual, is(likeCount + ""));
    }

    public AnswerUpdateFormPage goToUpdateAnswerPage() {
        driver.findElement(By.cssSelector("a.link-modify-article")).click();
        return new AnswerUpdateFormPage(driver);
    }

    public QuestionFormPage goToUpdatePage() {
        driver.findElement(By.cssSelector("a.link-modify-article")).click();
        return new QuestionFormPage(driver);
    }

    public void verifyBestAnswer() {
        String answerBest = driver.findElement(By.cssSelector(".qna-best-comment-label > strong")).getText();
        assertThat(answerBest, is("BEST 의견"));
    }

	public QuestionsPage goToQuestionsPage() {
		driver.findElement(By.cssSelector("nav.site-nav > ul > li")).click();
		return new QuestionsPage(driver);
	}
}
