package net.slipp.user;

import net.slipp.LoginUser;
import net.slipp.qna.IndexPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FacebookPage {
    private WebDriver driver;

    public FacebookPage(WebDriver driver) {
        this.driver = driver;
    }

    public IndexPage login(LoginUser loginUser) {
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(loginUser.getEmail());
        driver.findElement(By.id("pass")).clear();
        driver.findElement(By.id("pass")).sendKeys(loginUser.getPassword());
        driver.findElement(By.id("persist_box")).click();
        driver.findElement(By.cssSelector("#loginbutton > input")).click();

        if (isFirstLogin()) {
            LoginPage loginPage = new LoginPage(driver);
            return loginPage.loginSlipp(loginUser.getNickName());
        }
        
        if (driver.findElements(By.cssSelector(".layerConfirm")).size() != 0) {
            driver.findElement(By.cssSelector(".layerConfirm")).click();
        }
        
        return new IndexPage(driver);
    }

    private boolean isFirstLogin() {
        return "로그인 :: SLiPP".equals(driver.getTitle());
    }
}
