package net.slipp;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.slipp.qna.FBLogoutPage;
import net.slipp.qna.IndexPage;
import net.slipp.service.user.FixedPasswordGenerator;
import net.slipp.support.AbstractATTest;
import net.slipp.user.ChangePasswordPage;
import net.slipp.user.LoginPage;
import net.slipp.user.ProfilePage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationAT extends AbstractATTest {
    private static Logger log = LoggerFactory.getLogger(AuthenticationAT.class);
    
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
    public void join_change_password_login() throws Exception {
        String userId = environment.getProperty("slipp.userId1") + System.currentTimeMillis();
        String nickName = environment.getProperty("slipp.nickName1");
        String email = environment.getProperty("slipp.email1");
        LoginPage loginPage = indexPage.goLoginPage();
        indexPage = loginPage.join(userId, nickName, email);
        assertThat(indexPage.isLoginStatus(), is(true));
        ProfilePage profilePage = indexPage.goProfilePage();
        profilePage.verifyPageTitle(nickName);
        ChangePasswordPage changePasswordPage = profilePage.goChangePasswordPage();
        
        String oldPassword = FixedPasswordGenerator.DEFAULT_FIXED_PASSWORD;
        String newPassword = "newPassword";
        changePasswordPage.changePassword(oldPassword, newPassword);
        indexPage = indexPage.logout();
        loginPage = indexPage.goLoginPage();
        loginPage.loginToSlipp(userId, newPassword);
        assertThat(indexPage.isLoginStatus(), is(true));
    }
    
    @After
    public void teardown() {
        FBLogoutPage logoutPage = new FBLogoutPage(driver);
        logoutPage.logout();
    }
}
