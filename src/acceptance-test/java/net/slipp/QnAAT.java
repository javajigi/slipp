package net.slipp;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import net.slipp.qna.AdminTagPage;
import net.slipp.qna.AnswerUpdateFormPage;
import net.slipp.qna.IndexPage;
import net.slipp.qna.QuestionFixture;
import net.slipp.qna.QuestionFormPage;
import net.slipp.qna.QuestionPage;
import net.slipp.support.AbstractATTest;

import org.junit.After;
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
    public void create_question_success() {
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
    public void newTags() {
        loginToFacebook(1);
        QuestionFormPage qnaFormPage = indexPage.goQuestionForm();
        questionFixture.setPlainTags("java javascript newtag");
        qnaFormPage.question(questionFixture);
        AdminTagPage adminTagPage = indexPage.goAdminTagPage();
    	assertThat(adminTagPage.existNewTag("newtag"), is(true));
    	adminTagPage = adminTagPage.moveToPoolTag();
    	assertThat(adminTagPage.existNewTag("newtag"), is(false));
    	
    }
    
    @Test
	public void 답변이_정상적으로_등록() throws Exception {
    	loginToFacebook(1);
    	createQuestion(questionFixture);
    	loginToAnotherUser(2);
    	answerToQuestion();
	}
    
    @Test
	public void 답변_수정() throws Exception {
    	loginToFacebook(1);
    	createQuestion(questionFixture);
    	loginToAnotherUser(2);
    	QuestionPage questionPage = answerToQuestion();
    	AnswerUpdateFormPage answerFormPage = questionPage.goToUpdateAnswerPage();
    	String answer = "이 답변은 수정 답변입니다.";
    	questionPage = answerFormPage.updateAnswer(answer);
    	questionPage.verifyAnswer(answer);
	}

	private void logout() {
		indexPage.logout();
	}

    @Test
	public void 로그인과_로그아웃_답변에_대한_공감() throws Exception {
    	loginToFacebook(1);
    	createQuestion(questionFixture);
        loginToAnotherUser(2);
        QuestionPage questionPage = answerToQuestion();
        questionPage = questionPage.likeAnswer();
        questionPage.verifyLikeCount(1);
        
    	logout();
    	indexPage.goTopQuestion();
    	questionPage.likeAnswer();
    	verifyLoginPage();
	}

    private void loginToAnotherUser(int userNo) {
        logout();
        loginToFacebook(userNo);
    }
    
    @Test
    public void 베스트_답변() throws Exception {
        loginToFacebook(1);
        createQuestion(questionFixture);
        loginToAnotherUser(2);
        answerToQuestion();
        
        QuestionPage questionPage = likeAnswer(3, 1);
        questionPage = likeAnswer(4, 2);
        questionPage = likeAnswer(5, 3);
        
        questionPage.verifyBestAnswer();
    }

    private QuestionPage likeAnswer(int userNo, int likeCount) {
        loginToAnotherUser(userNo);
        QuestionPage questionPage = indexPage.goTopQuestion();
        questionPage = questionPage.likeAnswer();
        questionPage.verifyLikeCount(likeCount);
        return questionPage;
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
    
    @After
    public void tearDown() {
        logout();
    }
}
