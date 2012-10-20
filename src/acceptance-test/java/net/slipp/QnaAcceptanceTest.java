package net.slipp;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.slipp.qna.AdminTagPage;
import net.slipp.qna.IndexPage;

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
	}

	@Test
	public void 태그_관리자_탭() throws Exception {
		home.goAdminTagPage();		
	}
	
	@Test
	public void 태그_추가() throws Exception {
		AdminTagPage adminTag = home.goAdminTagPage();		
		adminTag = adminTag.createTag("newTag");
		adminTag.validateNewTag("newTag");
	}
	
	@Test
	public void 태그_중복_추가() throws Exception {
		AdminTagPage adminTag = home.goAdminTagPage();	
		adminTag = adminTag.createTag("newTag1").createTag("newTag1");
		assertThat(adminTag.hasDuplidateErrorMessage(), is(true));
	}
}
