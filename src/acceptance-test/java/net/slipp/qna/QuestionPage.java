package net.slipp.qna;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.openqa.selenium.WebDriver;

public class QuestionPage {
	private WebDriver driver;

	public QuestionPage(WebDriver driver, String title) {
		this.driver = driver;
		assertThat(driver.getTitle(), is(title));
	}

    public void verify(String title, String contents, String plainTags) {
        
    }
}
