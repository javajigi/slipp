package net.slipp.qna

import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import java.util.List
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class QuestionsPage {
  private var driver: WebDriver = null

  def this(driver: WebDriver) {
    this()
    this.driver = driver
  }

  def verifyFirstTitle(title: String) {
    val firstQuestion: WebElement = findQuestion(0)
    val actual: String = firstQuestion.findElement(By.cssSelector("strong.subject > a")).getText
    assertThat(actual, is(title))
  }

  def verifyFirstNickName(nickName: String) {
    val firstQuestion: WebElement = findQuestion(0)
    val actual: String = firstQuestion.findElement(By.cssSelector("div.auth-info > a.author")).getText
    assertThat(actual, is(nickName))
  }

  def verifyFirstAnswerCount(answerCount: Int) {
    val firstQuestion: WebElement = findQuestion(0)
    val actual: String = firstQuestion.findElement(By.cssSelector("div.reply > span.point")).getText
    assertThat(actual, is(answerCount + ""))
  }

  private def findQuestion(index: Int): WebElement = {
    return findQuestions.get(index)
  }

  private def findQuestions: List[WebElement] = {
    return driver.findElements(By.cssSelector("ul.list > li"))
  }
}
