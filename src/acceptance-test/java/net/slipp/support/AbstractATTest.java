package net.slipp.support;


public class AbstractATTest {
	protected SharedDriver driver;
	protected SlippEnvironment environment;

	public void setup() {
        this.driver = new SharedDriver();
        deleteAllCookies(driver);
        this.environment = new SlippEnvironment();
	}

	protected void deleteAllCookies(SharedDriver sharedDriver) {
		sharedDriver.deleteAllCookies();
	}
}
