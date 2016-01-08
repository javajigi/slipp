package net.slipp.qna

import net.slipp.LoginUser
import net.slipp.domain.tag.RequestTagPage
import net.slipp.user.{FacebookPage, GooglePage, LoginPage, ProfilePage, TwitterPage}
import org.hamcrest.CoreMatchers.is
import org.junit.Assert._
import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}
import org.openqa.selenium.{By, WebDriver}
import org.slf4j.{Logger, LoggerFactory}

class IndexPage {
  private val log: Logger = LoggerFactory.getLogger(classOf[IndexPage])
  private var driver: WebDriver = null

  def this(driver: WebDriver) {
    this()
    this.driver = driver
  }

  def loginToFacebook(email: String, password: String, nickName: String): IndexPage = {
    loginToFacebook(new LoginUser(email, password, nickName))
  }

  def loginToFacebook(loginUser: LoginUser): IndexPage = {
    log.debug("loginUser : {}", loginUser)
    new WebDriverWait(driver, 1000).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.link-loginout")))
    driver.findElement(By.cssSelector("a.link-loginout")).click()
    driver.findElement(By.cssSelector(".btn-login-facebook")).click()
    if (driver.getTitle == "SLiPP") {
      new IndexPage(driver)
    }
    val facebookPage: FacebookPage = new FacebookPage(driver)
    facebookPage.login(loginUser)
  }

  def loginToGoogle(username: String, password: String): IndexPage = {
    driver.findElement(By.cssSelector(".loginBtn > a")).click()
    driver.findElement(By.cssSelector(".btn-login-google")).click()
    if (driver.getTitle == "SLiPP") {
      new IndexPage(driver)
    }
    val googlePage: GooglePage = new GooglePage(driver)
    googlePage.login(username, password)
  }

  def loginToTwitter(username: String, password: String): IndexPage = {
    driver.findElement(By.cssSelector(".loginBtn > a")).click()
    driver.findElement(By.cssSelector("input[value='트위터 계정으로 로그인']")).click()
    if (driver.getTitle == "SLiPP") {
      new IndexPage(driver)
    }
    val twitterPage: TwitterPage = new TwitterPage(driver)
    twitterPage.login(username, password)
  }

  def logout: IndexPage = {
    val logoutPage: FBLogoutPage = new FBLogoutPage(driver)
    logoutPage.logout
    if (isLoginStatus) {
      driver.findElement(By.cssSelector("a.link-loginout")).click()
    }
    new IndexPage(driver)
  }

  def goQuestionForm: QuestionFormPage = {
    driver.findElement(By.id("writeBtn")).click()
    new QuestionFormPage(driver)
  }

  def goQuestionsPage: QuestionsPage = {
    driver.findElement(By.linkText("QnA")).click()
    new QuestionsPage(driver)
  }

  def goIndexPage: IndexPage = {
    driver.findElement(By.cssSelector("h1.logo")).click()
    new IndexPage(driver)
  }

  def goToQuestion(index: Int): QuestionPage = {
    val questions = driver.findElements(By.cssSelector("section.qna-list > ul > li"))
    questions.get(index).findElement(By.cssSelector("strong.subject > a")).click()
    new QuestionPage(driver)
  }

  def goLoginPage: LoginPage = {
    driver.findElement(By.cssSelector("a.link-loginout")).click()
    new LoginPage(driver)
  }

  def isLoginStatus: Boolean = {
    val logouts = driver.findElements(By.cssSelector("a[title='로그아웃']"))
    logouts.size == 1
  }

  def goProfilePage: ProfilePage = {
    driver.findElement(By.cssSelector("li.user-info > a > img.user-thumb")).click()
    driver.findElement(By.cssSelector("a.link-to-personalize")).click()
    new ProfilePage(driver)
  }

  def goAdminPage: AdminQuestionPage = {
    driver.findElement(By.cssSelector("#adminManagement")).click()
    new AdminQuestionPage(driver)
  }

  def goRequestTagPage: RequestTagPage = {
    driver.findElement(By.cssSelector("#requestTag")).click()
    new RequestTagPage(driver)
  }

  def verifyAdminPage() = {
    val adminMenu = driver.findElement(By.cssSelector("#adminManagement > span.text")).getText
    assertThat(adminMenu, is("관리도구"))
  }

  def verifyNonAdminPage() = {
    val adminElements = driver.findElements(By.cssSelector("#adminManagement"))
    assertTrue(adminElements.isEmpty)
  }
}
