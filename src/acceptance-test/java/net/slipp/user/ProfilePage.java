package net.slipp.user;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.openqa.selenium.WebDriver;

public class ProfilePage {
    private WebDriver driver;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    public void verifyPageTitle(String nickName) {
        assertThat(driver.getTitle(), is(nickName + " 공간 :: SLiPP"));
    }

}
