package net.slipp.qna

import java.util.List
import java.util.concurrent.TimeUnit

import com.google.common.collect.Lists
import org.hamcrest.CoreMatchers.is
import org.junit.Assert._
import org.openqa.selenium.{By, WebDriver, WebElement}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConversions._

class QuestionPage(driver: WebDriver, title: String) {
  private val log: Logger = LoggerFactory.getLogger(classOf[QuestionPage])

  // assertThat(driver.getTitle, is(title))

  def this(driver: WebDriver) = this(driver, driver.getTitle)

  def verify(title: String, contents: String, plainTags: String) {
    val titleElement: WebElement = driver.findElement(By.cssSelector(".qna-title"))
    assertThat(titleElement.getText, is(title))
    val contentsElement = driver.findElement(By.cssSelector(".article-doc > p"))
    assertThat(contentsElement.getText, is(contents))
    verifyTags(plainTags)
  }

  def verifyTags(plainTags: String) = {
    val tags = plainTags.split(" ")
    val tagElements = driver.findElements(By.cssSelector("section.qna-tags > ul > li"))
    val addedTags = tagElements.map(e => e.findElement(By.cssSelector("a")).getText)
    tags.foreach(tag => {
      log.debug(s"tag : ${tag}")
      assertTrue(addedTags.contains(tag))
    })
  }

  def answer(answer: String) {
    driver.findElement(By.id("contents")).clear
    driver.findElement(By.id("contents")).sendKeys(answer)
    driver.findElement(By.cssSelector(".btn-submit")).click()
  }

  def verifyAnswer(answer: String) {
    val commentTexts: List[String] = findAnswerTexts
    log.debug("comments size : {}", commentTexts.size)
  }

  private def findAnswerTexts = {
    val comments = driver.findElements(By.cssSelector("div.comment-doc"))
    val commentTexts = Lists.newArrayList[String]()
    for (comment <- comments) {
      commentTexts.add(comment.findElement(By.cssSelector("p")).getText)
    }
    commentTexts
  }

  def deleteAnswer() {
    driver.findElement(By.cssSelector("button.link-delete-article")).click()
    
    val alert = driver.switchTo.alert
    alert.accept()
  }

  def verifyAnswerCount(answerCount: String) {
    val actual: String = driver.findElement(By.cssSelector(".qna-comment-count > strong")).getText
    assertThat(actual, is(answerCount))
    assertThat(findAnswerTexts.size(), is(answerCount.toInt))
  }

  def likeAnswer: QuestionPage = {
    driver.findElement(By.cssSelector("a.btn-like-article")).click()
    driver.manage.timeouts.implicitlyWait(3, TimeUnit.SECONDS)
    return new QuestionPage(driver)
  }

  def verifyLikeCount(likeCount: Int) {
    Thread.sleep(100)
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

  def goToQuestionsPage = {
    driver.findElement(By.cssSelector("nav.site-nav > ul > li")).click()
    new QuestionsPage(driver)
  }

  def addTag(tag: String) = {
    val taggedForm = driver.findElement(By.id("taggedForm"))
    taggedForm.findElement(By.cssSelector("input.inp_nickname")).clear
    taggedForm.findElement(By.cssSelector("input.inp_nickname")).sendKeys(tag)
    taggedForm.findElement(By.cssSelector(".signin-with-sns-submit-btn")).click
    new QuestionPage(driver, driver.getTitle)
  }
}
