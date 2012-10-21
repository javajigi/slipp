package net.slipp.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.slipp.support.SharedDriver;
import net.slipp.support.SlippEnvironment;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

public class QnAStepDef {
    private SharedDriver driver;
    private SlippEnvironment environment;
    private QuestionFixture questionFixture;
    private IndexPage indexPage;
    private QuestionsFormPage qnaFormPage;
    private QuestionPage questionPage;
    
    public QnAStepDef(SharedDriver sharedDriver, SlippEnvironment environment) {
        this.driver = sharedDriver;
        this.environment = environment;
        questionFixture = new QuestionFixture();
        driver.get("http://localhost:8080");
    }
    
    @Given("^SLiPP 메인 페이지에서 로그인 후 글쓰기 버튼을 클릭한다.$")
    public void goCreateQuestionPage() {
        loginToFacebook();
        qnaFormPage = indexPage.goQuestionForm();
    }
    
    @When("^제목 (.*) 입력한다.$")
    public void writeTitle(String title) {
    	questionFixture.setTitle(title);
    }
    
    @When("^내용 (.*) 입력한다.$")
    public void writeContents(String contents) {
    	questionFixture.setContents(contents);
    }
    
    @When("^태그 (.*) 입력한다.$")
    public void writeTags(String plainTags) {
    	questionFixture.setPlainTags(plainTags);
    }
    
    @When("^질문하기 버튼을 클릭한다.$")
    public void question() {
        questionPage = qnaFormPage.question(questionFixture);
    }
    
    @Then("^입력한 데이터가 정상적으로 등록되었는지 확인한다. 태그는 (.*)가 등록되어야 한다.$")
    public void verifyQuestion(String expectedTags) {
    	questionPage.verify(questionFixture.getTitle(), questionFixture.getContents(), expectedTags);
    }
    
    @Then("^신규 태그 목록에서 (.*)를 볼 수 있어야 한다.$")
    public void verifyNewTag(String newTag) {
    	NewTagsPage newTagsPage = questionPage.goNewTagsPage();
    	assertThat(newTagsPage.existNewTag(newTag), is(true));
    }
    
    @Given("^질문을 등록한다.$")
    public void createQuestion() {
        loginToFacebook();
        qnaFormPage = indexPage.goQuestionForm();
        questionPage = qnaFormPage.question(questionFixture);
    }
    
    @When("^답변 (.*) 를 입력한다.$")
    public void answerToQuestion(String answer) {
    	indexPage.logout();
    	loginToTwitter();
    	questionPage.answer(answer);
    }
    
    private void loginToFacebook() {
        indexPage = new IndexPage(driver);
        indexPage = indexPage.loginToFacebook(environment.getProperty("facebook.email"), environment.getProperty("facebook.password"));
    }
    
    private void loginToTwitter() {
        indexPage = new IndexPage(driver);
        indexPage = indexPage.loginToTwitter(environment.getProperty("twitter.username"), environment.getProperty("twitter.password"));
    }
    
    @Then("^답변 (.*) 정확하게 등록되어야 한다.$")
    public void verifyAnswerContents(String answer) {
    	questionPage.verifyAnswer(answer);
    }
    
    @Then("^질문의 답변 숫자가 (.*)이어야 한다.$")
    public void verifyAnswerCount(String answerCount) {
    	questionPage.verifyAnswerCount(answerCount);
    }
    
    
}
