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
import net.slipp.web.UserForm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;

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
        String firstNickName = environment.getProperty("facebook.nickName1");
        indexPage.loginToFacebook(
                environment.getProperty("facebook.email1"), 
                environment.getProperty("facebook.password1"),
                firstNickName);
        
        FBLogoutPage logoutPage = new FBLogoutPage(driver);
        logoutPage.verifyCurrentLoginUser(firstNickName);
        logoutPage.logout();
        
        String secondNickName = environment.getProperty("facebook.nickName2");
        indexPage.loginToFacebook(
                environment.getProperty("facebook.email2"), 
                environment.getProperty("facebook.password2"), 
                secondNickName);
        logoutPage.goToLogoutPage();
        logoutPage.verifyCurrentLoginUser(secondNickName);
    }
    
    @Test
    public void join() throws Exception {
        join_to_slipp();  
    }
    
    private UserForm join_to_slipp() throws Exception {
        String userId = createUserId();
        String email = System.currentTimeMillis() + environment.getProperty("slipp.email1");
        LoginPage loginPage = indexPage.goLoginPage();
        indexPage = loginPage.join(userId, email);
        assertThat(indexPage.isLoginStatus(), is(true));
        return new UserForm(userId, email);
    }

    private String createUserId() {
        return environment.getProperty("slipp.userId1") + System.currentTimeMillis();
    }
    
    @Test
    public void join_relogin() throws Exception {
        UserForm userForm = join_to_slipp();
        indexPage = indexPage.logout();
        LoginPage loginPage = indexPage.goLoginPage();
        loginPage.loginToSlipp(userForm.getEmail(), FixedPasswordGenerator.DEFAULT_FIXED_PASSWORD);
        assertThat(indexPage.isLoginStatus(), is(true));
    }
    
    @Test
    public void join_change_password_login() throws Exception {
        UserForm userForm = join_to_slipp();
        ProfilePage profilePage = indexPage.goProfilePage();
        profilePage.verifyPageTitle(userForm.getUserId());
        ChangePasswordPage changePasswordPage = profilePage.goChangePasswordPage();
        
        String oldPassword = FixedPasswordGenerator.DEFAULT_FIXED_PASSWORD;
        String newPassword = "newPassword";
        changePasswordPage.changePassword(oldPassword, newPassword);
        Alert alert = driver.switchTo().alert();
        alert.accept();
        LoginPage loginPage = indexPage.goLoginPage();
        loginPage.loginToSlipp(userForm.getEmail(), newPassword);
        assertThat(indexPage.isLoginStatus(), is(true));
    }
    
    @After
    public void teardown() {
        FBLogoutPage logoutPage = new FBLogoutPage(driver);
        logoutPage.logout();
    }
}
