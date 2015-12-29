package net.slipp.qna

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class QuestionFormPage(driver: WebDriver) {
  def question(questionFixture: QuestionFixture): QuestionPage = {
    driver.findElement(By.id("title")).clear
    driver.findElement(By.id("title")).sendKeys(questionFixture.getTitle)
    driver.findElement(By.id("contents")).clear
    driver.findElement(By.id("contents")).sendKeys(questionFixture.getContents)
    driver.findElement(By.id("plainTags")).clear
    driver.findElement(By.id("plainTags")).sendKeys(questionFixture.getPlainTags)
    driver.findElement(By.cssSelector(".btn-submit")).click
    return new QuestionPage(driver, questionFixture.getTitle)
  }
}
