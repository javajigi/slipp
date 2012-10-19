package net.slipp.qna;

import net.slipp.support.selenium.SharedDriver;
import net.slipp.user.FacebookPage;
import net.slipp.user.LoginPage;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
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
        indexPage = facebookPage.login("email", "password");
    }
    
    @Given("^질문 글쓰기 페이지로 이동한다.$")
    public void goCreateQuestionPage() {
        login();
    }
    
    @When("^(.*) 태그로 질문을 등록한다.$")
    public void createQuestion(String plainTags) {
        QuestionsFormPage qnaForm = indexPage.goQuestionForm();
        questionPage = qnaForm.question("title", "this is contents", plainTags);
    }
    
    @Then("^질문과 태그가 정상적으로 등록되었는지 확인한다.$")
    public void verifyQuestion() {
        questionPage.verify();
    }
}
