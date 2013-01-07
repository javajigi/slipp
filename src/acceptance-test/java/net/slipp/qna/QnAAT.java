package net.slipp.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.slipp.support.AbstractATTest;

import org.junit.Before;
import org.junit.Test;

public class QnAAT extends AbstractATTest {
    private QuestionFixture questionFixture;
    private IndexPage indexPage;
    private QuestionsFormPage qnaFormPage;
    private QuestionPage questionPage;
    
    @Before
    public void setup() {
    	super.setup();
    	questionFixture = new QuestionFixture();
    	driver.get("http://localhost:8080");
    }
    
    @Test
    public void 질문이_정상적으로_등록() {
        loginToFacebook();
        qnaFormPage = indexPage.goQuestionForm();
        questionPage = qnaFormPage.question(questionFixture);
        questionPage.verify(questionFixture.getTitle(), questionFixture.getContents(), "java javascript");
    }
    
    @Test
    public void 신규태그_정상적으로_등록() {
        loginToFacebook();
        qnaFormPage = indexPage.goQuestionForm();
        questionFixture.setPlainTags("java javascript newtag");
        questionPage = qnaFormPage.question(questionFixture);
        NewTagsPage newTagsPage = questionPage.goNewTagsPage();
    	assertThat(newTagsPage.existNewTag("newtag"), is(true));
    }
    
    @Test
	public void 답변이_정상적으로_등록() throws Exception {
        loginToFacebook();
        qnaFormPage = indexPage.goQuestionForm();
        questionPage = qnaFormPage.question(questionFixture);
        indexPage.logout();
    	loginToTwitter();
    	String answer = "정확히 내가 바라는 답변이다.";
    	questionPage.answer(answer);
    	questionPage.verifyAnswer(answer);
    	questionPage.verifyAnswerCount("1");
	}
    
    private void loginToFacebook() {
        indexPage = new IndexPage(driver);
        indexPage = indexPage.loginToFacebook(environment.getProperty("facebook.email"), environment.getProperty("facebook.password"));
    }
    
    private void loginToTwitter() {
        indexPage = new IndexPage(driver);
        indexPage = indexPage.loginToTwitter(environment.getProperty("twitter.username"), environment.getProperty("twitter.password"));
    }
}
