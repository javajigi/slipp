package net.slipp.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.WebDriver;

public class AdminTagPage {
	private WebDriver driver;

	public AdminTagPage(WebDriver driver) {
		this.driver = driver;
		assertThat(driver.getTitle(), is("태그관리 :: SLiPP"));
	}

}
