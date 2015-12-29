package net.slipp.user

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class ChangePasswordPage(driver: WebDriver) {
  def changePassword(oldPassword: String, newPassword: String): ProfilePage = {
    driver.findElement(By.id("oldPassword")).clear
    driver.findElement(By.id("oldPassword")).sendKeys(oldPassword)
    driver.findElement(By.id("newPassword")).clear
    driver.findElement(By.id("newPassword")).sendKeys(newPassword)
    driver.findElement(By.id("newPasswordConfirm")).clear
    driver.findElement(By.id("newPasswordConfirm")).sendKeys(newPassword)
    driver.findElement(By.cssSelector(".person-change-pw-submit")).click
    return new ProfilePage(driver)
  }
}
