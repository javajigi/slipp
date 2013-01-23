package net.slipp;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Set;

import net.slipp.qna.AnswerUpdateFormPage;
import net.slipp.qna.IndexPage;
import net.slipp.qna.NewTagsPage;
import net.slipp.qna.QuestionFixture;
import net.slipp.qna.QuestionFormPage;
import net.slipp.qna.QuestionPage;
import net.slipp.support.AbstractATTest;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QnAAT extends AbstractATTest {
	private static final Logger log = LoggerFactory.getLogger(QnAAT.class);
	
    private QuestionFixture questionFixture;
    private IndexPage indexPage;
    
    @Before
    public void setup() {
    	super.setup();
    	questionFixture = new QuestionFixture();
    	driver.get("http://localhost:8080");
    }
    
    @Test
    public void 질문이_정상적으로_등록() {
    	loginToFacebook(1);
    	createQuestion(questionFixture);
    }
    
    @Test
    public void 정상적으로_수정() {
        loginToFacebook(1);
        QuestionPage questionPage = createQuestion(questionFixture);
        QuestionFormPage qnaFormPage = questionPage.goToUpdatePage();
        questionFixture.setTitle("update title");
        questionFixture.setContents("update contents");
        questionFixture.setPlainTags("java jsp servlet");
        questionPage = qnaFormPage.question(questionFixture);
        questionPage.verify(questionFixture.getTitle(), questionFixture.getContents(), questionFixture.getPlainTags());
    }

    @Test
    public void 신규태그_정상적으로_등록() {
        loginToFacebook(1);
        QuestionFormPage qnaFormPage = indexPage.goQuestionForm();
        questionFixture.setPlainTags("java javascript newtag");
        QuestionPage questionPage = qnaFormPage.question(questionFixture);
        NewTagsPage newTagsPage = questionPage.goNewTagsPage();
    	assertThat(newTagsPage.existNewTag("newtag"), is(true));
    }
    
    @Test
	public void 답변이_정상적으로_등록() throws Exception {
    	loginToFacebook(1);
    	createQuestion(questionFixture);
    	logout();
    	loginToFacebook(2);
    	answerToQuestion();
	}
    
    @Test
	public void 답변_수정() throws Exception {
    	loginToFacebook(1);
    	createQuestion(questionFixture);
    	logout();
    	loginToFacebook(2);
    	QuestionPage questionPage = answerToQuestion();
    	AnswerUpdateFormPage answerFormPage = questionPage.goToUpdateAnswerPage();
    	String answer = "이 답변은 수정 답변입니다.";
    	questionPage = answerFormPage.updateAnswer(answer);
    	questionPage.verifyAnswer(answer);
	}

	private void logout() {
		Options options = driver.manage();
		indexPage.logout();
		log.debug("Before Cookies.");
		printCookies(options.getCookies());
		options.deleteAllCookies();
		log.debug("After Cookies.");
		printCookies(options.getCookies());
	}

	private void printCookies(Set<Cookie> cookies) {
		for (Cookie cookie : cookies) {
			log.debug("cookie : {}", cookie);
		}
	}
    
    @Test
	public void 로그인과_로그아웃_답변에_대한_공감() throws Exception {
    	loginToFacebook(1);
    	createQuestion(questionFixture);
    	logout();
    	loginToFacebook(2);
    	QuestionPage questionPage = answerToQuestion();
    	questionPage.likeAnswer();
    	questionPage.verifyLikeCount("1");
    	
    	logout();
    	indexPage.goTopQuestion();
    	questionPage.likeAnswer();
    	verifyLoginPage();
	}

	private void verifyLoginPage() {
		assertThat(driver.getTitle(), is("로그인 :: SLiPP"));
	}

	private QuestionPage answerToQuestion() {
		String answer = "정확히 내가 바라는 답변이다.";
    	QuestionPage questionPage = indexPage.goTopQuestion();
    	questionPage.answer(answer);
    	questionPage.verifyAnswer(answer);
    	questionPage.verifyAnswerCount("1");
    	return questionPage;
	}

    
	private QuestionPage createQuestion(QuestionFixture questionFixture) {
        QuestionFormPage qnaFormPage = indexPage.goQuestionForm();
        QuestionPage questionPage = qnaFormPage.question(questionFixture);
        questionPage.verify(questionFixture.getTitle(), questionFixture.getContents(), questionFixture.getPlainTags());
        return questionPage;
	}
    
    private void loginToFacebook(int number) {
        indexPage = new IndexPage(driver);
        String email = environment.getProperty("facebook.email" + number);
        String password = environment.getProperty("facebook.password" + number);
        String nickName = "자바지기";
        if (number > 1) {
        	nickName = nickName + number;
        }
        indexPage = indexPage.loginToFacebook(email, password, nickName);
    }
}
