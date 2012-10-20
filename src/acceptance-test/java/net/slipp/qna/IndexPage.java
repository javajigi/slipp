package net.slipp.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.slipp.user.FacebookPage;
import net.slipp.user.LoginPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class IndexPage {
	private WebDriver driver;

	public IndexPage(WebDriver driver) {
		this.driver = driver;
		assertThat(driver.getTitle(), is("SLiPP"));
	}

	public IndexPage login(String username, String password) {
		driver.findElement(By.cssSelector(".loginBtn > a")).click();
		driver.findElement(By.cssSelector("input[value='페이스북 계정으로 로그인']")).click();
		if (driver.getTitle().equals("SLiPP")) {
			return new IndexPage(driver);
		}

		FacebookPage facebookPage = new FacebookPage(driver);
        return facebookPage.login(username, password);
	}
	
	public boolean logout() {
		WebElement webElement = driver.findElement(By.cssSelector(".loginBtn > a"));
		if (webElement == null) {
			return false;
		} else {
			return true;
		}
	}

	public AdminTagPage goAdminTagPage() {
		driver.findElement(By.cssSelector("#tagManagement > a")).click();
		return new AdminTagPage(driver);
	}

	public QuestionsFormPage goQuestionForm() {
		driver.findElement(By.id("questionBtn")).click();
		return new QuestionsFormPage(driver);
	}
}
