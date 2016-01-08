package net.slipp.qna

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class AnswerUpdateFormPage(driver: WebDriver) {
  def updateAnswer(answer: String): QuestionPage = {
    driver.findElement(By.id("contents")).clear
    driver.findElement(By.id("contents")).sendKeys(answer)
    driver.findElement(By.cssSelector(".btn-submit")).click
    return new QuestionPage(driver)
  }
}
