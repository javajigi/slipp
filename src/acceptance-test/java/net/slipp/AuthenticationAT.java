package net.slipp;

import net.slipp.qna.FBLogoutPage;
import net.slipp.qna.IndexPage;
import net.slipp.support.AbstractATTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuthenticationAT extends AbstractATTest {
    private IndexPage indexPage;
    
    @Before
    public void setup() {
        super.setup();
        driver.get("http://localhost:8080");
        indexPage = new IndexPage(driver);
    }
    
    @Test
    public void 로그인_후_재로그인() throws Exception {
        String firstNickName = "firstNickName";
        indexPage.loginToFacebook(
                environment.getProperty("facebook.email1"), 
                environment.getProperty("facebook.password1"), 
                firstNickName);
        
        FBLogoutPage logoutPage = new FBLogoutPage(driver);
        logoutPage.verifyCurrentLoginUser(firstNickName);
        logoutPage.logout();
        
        String secondNickName = "secondNickName";
        indexPage.loginToFacebook(
                environment.getProperty("facebook.email2"), 
                environment.getProperty("facebook.password2"), 
                secondNickName);
        logoutPage.goToLogoutPage();
        logoutPage.verifyCurrentLoginUser(secondNickName);
    }
    
    @After
    public void teardown() {
        FBLogoutPage logoutPage = new FBLogoutPage(driver);
        logoutPage.logout();
    }
}
