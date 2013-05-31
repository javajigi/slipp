package net.slipp.support;

import net.slipp.LoginUser;
import net.slipp.qna.IndexPage;


public class AbstractATTest {
	protected SharedDriver driver;
	protected SlippEnvironment environment;
    protected IndexPage indexPage;

	public void setup() {
        this.driver = new SharedDriver();
        deleteAllCookies(driver);
        this.environment = new SlippEnvironment();
	}

	protected void deleteAllCookies(SharedDriver sharedDriver) {
		sharedDriver.deleteAllCookies();
	}
	
	protected LoginUser loginToFacebook(int number) {
        indexPage = new IndexPage(driver);
        LoginUser loginUser = getLoginUser(number);
        indexPage = indexPage.loginToFacebook(loginUser);
        return loginUser;
    }
    
    private LoginUser getLoginUser(int number) {
          String email = environment.getProperty("facebook.email" + number);
          String password = environment.getProperty("facebook.password" + number);
          String nickName = environment.getProperty("facebook.nickName" + number);
          return new LoginUser(email, password, nickName);
    }
}
