package net.slipp.qna;

import java.util.List;

import net.slipp.LoginUser;
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
	
	public IndexPage loginToFacebook(String email, String password, String nickName) {
		return loginToFacebook(new LoginUser(email, password, nickName));
	}
	
	public IndexPage loginToFacebook(LoginUser loginUser) {
		log.debug("loginUser : {}", loginUser);
		
		driver.findElement(By.cssSelector("a.link-loginout")).click();
		driver.findElement(By.cssSelector("input[value='페이스북 계정으로 로그인']")).click();
		if (driver.getTitle().equals("SLiPP")) {
			return new IndexPage(driver);
		}

		FacebookPage facebookPage = new FacebookPage(driver);
        return facebookPage.login(loginUser);
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
	    FBLogoutPage logoutPage = new FBLogoutPage(driver);
        logoutPage.logout();
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
		driver.findElement(By.id("writeBtn")).click();
		return new QuestionFormPage(driver);
	}

	public QuestionsPage goQuestionsPage() {
		driver.findElement(By.linkText("QnA")).click();
		return new QuestionsPage(driver);
	}
	
	public IndexPage goIndexPage() {
		driver.findElement(By.cssSelector("a.brand")).click();
		return new IndexPage(driver);
	}
	
	public QuestionPage goToQuestion(int index) {
		List<WebElement> questions = driver.findElements(By.cssSelector("div.main > strong.subject > a"));
		questions.get(index).click();
		return new QuestionPage(driver);
	}
}
