package net.slipp.qna;

import net.slipp.support.selenium.SharedDriver;
import net.slipp.user.FacebookPage;
import net.slipp.user.LoginPage;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.When;

public class QnAStepDef {
    private SharedDriver driver;
    private IndexPage indexPage;
    private QuestionPage questionPage;
    
    public QnAStepDef(SharedDriver sharedDriver) {
        this.driver = sharedDriver;
        driver.get("http://localhost:8080");
    }
    
    private void login() {
        indexPage = new IndexPage(driver);
        LoginPage loginPage = indexPage.goLoginPage();
        FacebookPage facebookPage = loginPage.loginFacebook();
        indexPage = facebookPage.login("javajigi@gmail.com", "dPdms1129");
    }
    
    @Given("^SLiPP 메인 페이지에서 로그인 후 글쓰기 버튼을 클릭한다.$")
    public void goCreateQuestionPage() {
        login();
    }
    
    @When("^제목 : (.*), 내용 : (.*), 태그 : (.*) 를 등록한다.$")
    public void createQuestion(String title, String contents, String plainTags) {
        QuestionsFormPage qnaForm = indexPage.goQuestionForm();
        questionPage = qnaForm.question(title, contents, plainTags);
    }
    
    @When("^제목 : (.*), 내용 : (.*), 태그 : (.*) 가 정상적으로 등록되었는지 확인한다.$")
    public void verifyQuestion(String title, String contents, String plainTags) {
        questionPage.verify(title, contents, plainTags);
    }
}
