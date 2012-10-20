package net.slipp.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
    
}
