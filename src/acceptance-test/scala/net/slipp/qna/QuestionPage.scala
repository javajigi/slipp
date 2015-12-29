package net.slipp.qna

import java.util.List
import java.util.Arrays
import java.util.concurrent.TimeUnit

import com.google.common.collect.Lists
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.openqa.selenium.{By, WebDriver, WebElement}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConversions._

class QuestionPage(driver: WebDriver, title: String) {
  private val log: Logger = LoggerFactory.getLogger(classOf[QuestionPage])

  assertThat(driver.getTitle, is(title))

  def this(driver: WebDriver) = this(driver, driver.getTitle)

  def verify(title: String, contents: String, plainTags: String) {
    val titleElement: WebElement = driver.findElement(By.cssSelector(".qna-title"))
    assertThat(titleElement.getText, is(title))
    val tags = Arrays.asList(plainTags.split(" "))
    val tagElements = driver.findElements(By.cssSelector("doc > div.tags > ul > li"))
    for (each <- tagElements) {
      val tag: String = each.findElement(By.cssSelector("a")).getText
      assertThat(tags.contains(tag), is(true))
    }
  }

  def answer(answer: String) {
    driver.findElement(By.id("contents")).clear
    driver.findElement(By.id("contents")).sendKeys(answer)
    driver.findElement(By.cssSelector(".btn-submit")).click()
  }

  def verifyAnswer(answer: String) {
    val commentTexts: List[String] = findCommentTexts
    log.debug("comments size : {}", commentTexts.size)
  }

  private def findCommentTexts = {
    val comments = driver.findElements(By.cssSelector("div.comment-doc"))
    val commentTexts = Lists.newArrayList[String]()
    for (comment <- comments) {
      commentTexts.add(comment.findElement(By.cssSelector("p")).getText)
    }
    commentTexts
  }

  def verifyAnswerCount(answerCount: String) {
    val actual: String = driver.findElement(By.cssSelector(".qna-comment-count > strong")).getText
    assertThat(actual, is(answerCount))
  }

  def likeAnswer: QuestionPage = {
    driver.findElement(By.cssSelector("a.btn-like-article")).click()
    driver.manage.timeouts.implicitlyWait(3, TimeUnit.SECONDS)
    return new QuestionPage(driver)
  }

  def verifyLikeCount(likeCount: Int) {
    val actual: String = driver.findElement(By.cssSelector("a.btn-like-article > strong.like-article-count")).getText
    assertThat(actual, is(likeCount + ""))
  }

  def goToUpdateAnswerPage: AnswerUpdateFormPage = {
    driver.findElement(By.cssSelector("a.link-modify-article")).click()
    return new AnswerUpdateFormPage(driver)
  }

  def goToUpdatePage: QuestionFormPage = {
    driver.findElement(By.cssSelector("a.link-modify-article")).click()
    return new QuestionFormPage(driver)
  }

  def verifyBestAnswer {
    val answerBest: String = driver.findElement(By.cssSelector(".qna-best-comment-label > strong")).getText
    assertThat(answerBest, is("BEST 의견"))
  }

  def goToQuestionsPage: QuestionsPage = {
    driver.findElement(By.cssSelector("nav.site-nav > ul > li")).click()
    return new QuestionsPage(driver)
  }
}
