package net.slipp.qna

import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.openqa.selenium.{By, NoSuchElementException, WebDriver, WebElement}

import scala.collection.JavaConversions._

class AdminTagPage(driver: WebDriver) {
  assertThat(driver.getTitle, is("태그관리 :: SLiPP"))

  def createTag(newTag: String): AdminTagPage = {
    driver.findElement(By.id("name")).clear
    driver.findElement(By.id("name")).sendKeys(newTag)
    driver.findElement(By.cssSelector("#tagForm > button")).click
    return new AdminTagPage(driver)
  }

  def validateNewTag(newTag: String): AdminTagPage = {
    val tags: WebElement = driver.findElement(By.xpath("//tbody/tr[1]/td[2]"))
    assertThat(tags.getText, is(newTag))
    return this
  }

  def existNewTag(newTag: String): Boolean = {
    val newTags: List[String] = findAllNewTags
    return newTags.contains(newTag)
  }

  private def findAllNewTags: List[String] = {
    val newTags = List[String]()
    val tagElements = driver.findElements(By.xpath("//tbody/tr[@class='newTags']/td[1]"))
    for (tagElement <- tagElements) {
      newTags.add(tagElement.getText)
    }
    return newTags
  }

  def hasDuplidateErrorMessage: Boolean = {
    try {
      driver.findElement(By.cssSelector("label.error"))
      return true
    }
    catch {
      case e: NoSuchElementException => {
        return false
      }
    }
  }

  def moveToPoolTag: AdminTagPage = {
    driver.findElement(By.id("moveToPoolTagBtn")).click
    return this
  }
}
