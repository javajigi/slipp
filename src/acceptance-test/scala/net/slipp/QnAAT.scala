package net.slipp

import java.util.List

import net.slipp.qna.{AnswerUpdateFormPage, QuestionFixture, QuestionFormPage, QuestionPage, QuestionsPage}
import net.slipp.support.AbstractATTest
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.junit.{After, Before, Test}
import org.openqa.selenium.{By, WebElement}
import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}

class QnAAT extends AbstractATTest {
  private var questionFixture: QuestionFixture = null

  @Before override def setup() {
    super.setup
    questionFixture = new QuestionFixture
    driver.get("http://localhost:8080")
  }

  @Test def create_question_success {
    loginToFacebook(1)
    createQuestion(questionFixture)
  }

  @Test def update_question_success {
    loginToFacebook(1)
    var questionPage: QuestionPage = createQuestion(questionFixture)
    val qnaFormPage: QuestionFormPage = questionPage.goToUpdatePage
    questionFixture.setTitle("update title")
    questionFixture.setContents("update contents")
    questionFixture.setPlainTags("java jsp servlet")
    questionPage = qnaFormPage.question(questionFixture)
    questionPage.verify(questionFixture.getTitle, questionFixture.getContents, questionFixture.getPlainTags)
  }

  @Test def newTags {
    loginToFacebook(1)
    val qnaFormPage: QuestionFormPage = indexPage.goQuestionForm
    questionFixture.setPlainTags("java javascript newtag")
    qnaFormPage.question(questionFixture)
  }

  @Test
  @throws(classOf[Exception])
  def create_answer {
    loginToFacebook(1)
    createQuestion(questionFixture)
    loginToAnotherUser(2)
    answerToQuestion
    loginToAnotherUser(1)
    assertThat(countNotifications > 0, is(true))
    clickNotificationBtn
  }

  private def clickNotificationBtn {
    driver.findElement(By.cssSelector("li.user-info > a > span.user-name")).click
    new WebDriverWait(driver, 1000).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#notificationLayer > ul > li")))
    val elements: List[WebElement] = driver.findElements(By.cssSelector("#notificationLayer > ul > li"))
    assertThat(elements.size > 0, is(true))
    assertThat(countNotifications, is(0))
  }

  private def countNotifications: Int = {
    val value: String = driver.findElement(By.cssSelector("span.notification-count")).getText
    value.toInt
  }

  @Test
  @throws(classOf[Exception])
  def question_list_when_create_answer {
    loginToFacebook(1)
    val firstTitle: String = "this is first question."
    questionFixture.setTitle(firstTitle)
    createQuestion(questionFixture)
    indexPage.goIndexPage
    val secondTitle: String = "this is second question."
    questionFixture.setTitle(secondTitle)
    createQuestion(questionFixture)
    val loginUser: LoginUser = loginToAnotherUser(2)
    val questionPage: QuestionPage = answerToQuestion(1)
    val questionsPage: QuestionsPage = questionPage.goToQuestionsPage
    questionsPage.verifyFirstTitle(firstTitle)
    questionsPage.verifyFirstNickName(loginUser.nickName)
    questionsPage.verifyFirstAnswerCount(1)
  }

  @Test
  @throws(classOf[Exception])
  def update_answer {
    loginToFacebook(1)
    createQuestion(questionFixture)
    loginToAnotherUser(2)
    var questionPage: QuestionPage = answerToQuestion
    val answerFormPage: AnswerUpdateFormPage = questionPage.goToUpdateAnswerPage
    val answer: String = "이 답변은 수정 답변입니다."
    questionPage = answerFormPage.updateAnswer(answer)
    questionPage.verifyAnswer(answer)
  }

  private def logout() {
    indexPage.logout
  }

  @Test
  @throws(classOf[Exception])
  def answer_best {
    loginToFacebook(1)
    createQuestion(questionFixture)
    loginToAnotherUser(2)
    var questionPage: QuestionPage = answerToQuestion
    questionPage = questionPage.likeAnswer
    questionPage.verifyLikeCount(1)
    logout
    indexPage.goToQuestion(0)
    questionPage.likeAnswer
  }

  private def loginToAnotherUser(userNo: Int): LoginUser = {
    logout
    loginToFacebook(userNo)
  }

  @Test
  @throws(classOf[Exception])
  def best_answer {
    loginToFacebook(1)
    createQuestion(questionFixture)
    loginToAnotherUser(2)
    answerToQuestion
    var questionPage: QuestionPage = likeAnswer(3, 1)
    questionPage = likeAnswer(4, 2)
    questionPage = likeAnswer(5, 3)
    questionPage.verifyBestAnswer
  }

  private def likeAnswer(userNo: Int, likeCount: Int): QuestionPage = {
    loginToAnotherUser(userNo)
    var questionPage: QuestionPage = indexPage.goToQuestion(0)
    questionPage = questionPage.likeAnswer
    questionPage.verifyLikeCount(likeCount)
    questionPage
  }

  private def answerToQuestion(index: Int): QuestionPage = {
    val answer: String = "정확히 내가 바라는 답변이다."
    val questionPage: QuestionPage = indexPage.goToQuestion(index)
    questionPage.answer(answer)
    questionPage.verifyAnswer(answer)
    questionPage.verifyAnswerCount("1")
    questionPage
  }

  private def answerToQuestion: QuestionPage = {
    answerToQuestion(0)
  }

  private def createQuestion(questionFixture: QuestionFixture): QuestionPage = {
    val qnaFormPage: QuestionFormPage = indexPage.goQuestionForm
    val questionPage: QuestionPage = qnaFormPage.question(questionFixture)
    questionPage.verify(questionFixture.getTitle, questionFixture.getContents, questionFixture.getPlainTags)
    questionPage
  }

  @After def tearDown() {
    logout()
  }
}
