package net.slipp.user;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProfilePage {
    private WebDriver driver;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    public void verifyPageTitle(String nickName) {
        assertThat(driver.getTitle(), is(nickName + "의 개인공간 :: SLiPP"));
    }

    public ChangePasswordPage goChangePasswordPage() {
        driver.findElement(By.linkText("비밀번호 변경하기")).click();
        return new ChangePasswordPage(driver);
    }

}
