package net.slipp;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.slipp.qna.AnswerUpdateFormPage;
import net.slipp.qna.IndexPage;
import net.slipp.qna.NewTagsPage;
import net.slipp.qna.QuestionFixture;
import net.slipp.qna.QuestionFormPage;
import net.slipp.qna.QuestionPage;
import net.slipp.support.AbstractATTest;

import org.junit.Before;
import org.junit.Test;

public class QnAAT extends AbstractATTest {
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
    	loginToFacebook();
    	createQuestion(questionFixture);
    }
    
    @Test
    public void 정상적으로_수정() {
        loginToFacebook();
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
        loginToFacebook();
        QuestionFormPage qnaFormPage = indexPage.goQuestionForm();
        questionFixture.setPlainTags("java javascript newtag");
        QuestionPage questionPage = qnaFormPage.question(questionFixture);
        NewTagsPage newTagsPage = questionPage.goNewTagsPage();
    	assertThat(newTagsPage.existNewTag("newtag"), is(true));
    }
    
    @Test
	public void 답변이_정상적으로_등록() throws Exception {
    	loginToFacebook();
    	createQuestion(questionFixture);
    	indexPage.logout();
    	loginToTwitter();
    	answerToQuestion();
	}
    
    @Test
	public void 답변_수정() throws Exception {
    	loginToFacebook();
    	createQuestion(questionFixture);
    	indexPage.logout();
    	loginToTwitter();
    	QuestionPage questionPage = answerToQuestion();
    	AnswerUpdateFormPage answerFormPage = questionPage.goToUpdateAnswerPage();
    	String answer = "이 답변은 수정 답변입니다.";
    	questionPage = answerFormPage.updateAnswer(answer);
    	questionPage.verifyAnswer(answer);
	}
    
    @Test
	public void 로그인과_로그아웃_답변에_대한_공감() throws Exception {
    	loginToFacebook();
    	createQuestion(questionFixture);
    	indexPage.logout();
    	loginToTwitter();
    	QuestionPage questionPage = answerToQuestion();
    	questionPage.likeAnswer();
    	questionPage.verifyLikeCount("1");
    	
    	indexPage.logout();
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
    
    private void loginToFacebook() {
        indexPage = new IndexPage(driver);
        indexPage = indexPage.loginToFacebook(environment.getProperty("facebook.email"), environment.getProperty("facebook.password"));
    }
    
    private void loginToTwitter() {
        indexPage = new IndexPage(driver);
        indexPage = indexPage.loginToTwitter(environment.getProperty("twitter.username"), environment.getProperty("twitter.password"));
    }
}
