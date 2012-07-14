package net.slipp.qna;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class QnaAcceptanceTest {
	private IndexPage dut;
	
	@Before
	public void setup() {
		WebDriver driver = new HtmlUnitDriver();
		dut = new IndexPage(driver);
	}
	
	@Test
	public void 페이스북_로그인() throws Exception {
		LoginPage loginPage = dut.goLoginPage();
	}
}
