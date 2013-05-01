package net.slipp;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import net.slipp.qna.FBLogoutPage;
import net.slipp.qna.IndexPage;
import net.slipp.support.AbstractATTest;
import net.slipp.user.LoginPage;

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
    public void login_after_relogin() throws Exception {
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
    
    @Test
    public void join_slipp_user() throws Exception {
        String userId = environment.getProperty("slipp.userId1");
        String nickName = environment.getProperty("slipp.nickName1");
        String email = environment.getProperty("slipp.email1");
        LoginPage loginPage = indexPage.goLoginPage();
        indexPage = loginPage.join(userId, nickName, email);
        assertThat(indexPage.isLoginStatus(), is(true));
    }
    
    @After
    public void teardown() {
        FBLogoutPage logoutPage = new FBLogoutPage(driver);
        logoutPage.logout();
    }
}
