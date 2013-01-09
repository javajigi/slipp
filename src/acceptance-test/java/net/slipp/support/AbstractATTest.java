package net.slipp.support;


public class AbstractATTest {
	protected SharedDriver driver;
	protected SlippEnvironment environment;

	public void setup() {
        this.driver = new SharedDriver();
        this.driver.deleteAllCookies();
        this.environment = new SlippEnvironment();
	}
}
