package net.slipp.qna;

import java.util.List;

import net.slipp.user.FacebookPage;
import net.slipp.user.GooglePage;
import net.slipp.user.TwitterPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexPage {
	private static final Logger log = LoggerFactory.getLogger(IndexPage.class); 
	private WebDriver driver;

	public IndexPage(WebDriver driver) {
		this.driver = driver;
	}

	public IndexPage loginToFacebook(String username, String password, String nickName) {
		log.debug("username : {}", username);
		
		driver.findElement(By.cssSelector(".loginBtn > a")).click();
		driver.findElement(By.cssSelector("input[value='페이스북 계정으로 로그인']")).click();
		if (driver.getTitle().equals("SLiPP")) {
			return new IndexPage(driver);
		}

		FacebookPage facebookPage = new FacebookPage(driver);
        return facebookPage.login(username, password, nickName);
	}
	
	public IndexPage loginToGoogle(String username, String password) {
		driver.findElement(By.cssSelector(".loginBtn > a")).click();
		driver.findElement(By.cssSelector("input[value='구글 계정으로 로그인']")).click();
		if (driver.getTitle().equals("SLiPP")) {
			return new IndexPage(driver);
		}

		GooglePage googlePage = new GooglePage(driver);
        return googlePage.login(username, password);
	}
	
	public IndexPage loginToTwitter(String username, String password) {
		driver.findElement(By.cssSelector(".loginBtn > a")).click();
		driver.findElement(By.cssSelector("input[value='트위터 계정으로 로그인']")).click();
		if (driver.getTitle().equals("SLiPP")) {
			return new IndexPage(driver);
		}

		TwitterPage twitterPage = new TwitterPage(driver);
        return twitterPage.login(username, password);
	}
	
	public IndexPage logout() {
	    driver.get("http://localhost:8080/fblogout");
	    try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
	    driver.findElement(By.id("fbLogoutBtn")).click();
	    List<WebElement> logoutLinks = driver.findElements(By.linkText("로그아웃"));
	    if (!logoutLinks.isEmpty()) {
	        logoutLinks.get(0).click();
	    }
		return new IndexPage(driver);
	}
	
	public AdminTagPage goAdminTagPage() {
		driver.findElement(By.cssSelector("#tagManagement > a")).click();
		return new AdminTagPage(driver);
	}

	public QuestionFormPage goQuestionForm() {
		driver.findElement(By.id("questionBtn")).click();
		return new QuestionFormPage(driver);
	}

	public QuestionsPage goQuestionsPage() {
		driver.findElement(By.linkText("QnA")).click();
		return new QuestionsPage(driver);
	}
	
	public QuestionPage goTopQuestion() {
		driver.findElement(By.cssSelector("div.main > strong.subject > a")).click();
		return new QuestionPage(driver);
	}
}
