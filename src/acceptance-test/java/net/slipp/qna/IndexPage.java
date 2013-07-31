package net.slipp.qna;

import java.util.List;

import net.slipp.LoginUser;
import net.slipp.tag.RequestTagPage;
import net.slipp.user.FacebookPage;
import net.slipp.user.GooglePage;
import net.slipp.user.LoginPage;
import net.slipp.user.ProfilePage;
import net.slipp.user.TwitterPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
		
		new WebDriverWait(driver, 1000).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.link-loginout")));
		driver.findElement(By.cssSelector("a.link-loginout")).click();
		driver.findElement(By.cssSelector(".btn-login-facebook")).click();
		if (driver.getTitle().equals("SLiPP")) {
			return new IndexPage(driver);
		}
		FacebookPage facebookPage = new FacebookPage(driver);
        return facebookPage.login(loginUser);
	}
	
	public IndexPage loginToGoogle(String username, String password) {
		driver.findElement(By.cssSelector(".loginBtn > a")).click();
		driver.findElement(By.cssSelector(".btn-login-google")).click();
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
        
        if (isLoginStatus()) {
            driver.findElement(By.cssSelector("a.link-loginout")).click();
        }
		return new IndexPage(driver);
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
		driver.findElement(By.cssSelector("h1.logo")).click();
		return new IndexPage(driver);
	}
	
	public QuestionPage goToQuestion(int index) {
		List<WebElement> questions = driver.findElements(By.cssSelector("section.qna-list > ul > li"));
		questions.get(index).findElement(By.cssSelector("strong.subject > a")).click();
		return new QuestionPage(driver);
	}

    public LoginPage goLoginPage() {
        driver.findElement(By.cssSelector("a.link-loginout")).click();
        return new LoginPage(driver);
    }
    
    public boolean isLoginStatus() {
        List<WebElement> logouts = driver.findElements(By.cssSelector("a[title='로그아웃']"));
        return logouts.size() == 1;
    }

    public ProfilePage goProfilePage() {
        driver.findElement(By.cssSelector("li.user-info > a > img.user-thumb")).click();
        driver.findElement(By.cssSelector("a.link-to-personalize")).click();
        return new ProfilePage(driver);
    }
    
    public AdminTagPage goAdminTagPage() {
        driver.findElement(By.cssSelector("#tagManagement")).click();
        return new AdminTagPage(driver);
    }

    public RequestTagPage goRequestTagPage() {
        driver.findElement(By.cssSelector("#requestTag")).click();
        return new RequestTagPage(driver);
    }
}
