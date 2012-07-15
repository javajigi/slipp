package net.slipp.qna;

import net.slipp.user.FacebookPage;
import net.slipp.user.LoginPage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext.xml")
public class QnaAcceptanceTest {
	private static final Logger logger = LoggerFactory.getLogger(QnaAcceptanceTest.class);
	
	@Value("#{applicationProperties['facebook.email']}")
	private String email;
	
	@Value("#{applicationProperties['facebook.password']}")
	private String password;
	
	private IndexPage home;
	
	@Before
	public void setup() {
		WebDriver driver = new HtmlUnitDriver();
		driver.get("http://localhost:8080");
		home = new IndexPage(driver);
	}
	
	@Test
	public void 페이스북_로그인() throws Exception {
		logger.debug("email : {}, password : {}", email, password);

		LoginPage loginPage = home.goLoginPage();
		FacebookPage facebookPage = loginPage.loginFacebook();
		home = facebookPage.login(email, password);
	}
}
