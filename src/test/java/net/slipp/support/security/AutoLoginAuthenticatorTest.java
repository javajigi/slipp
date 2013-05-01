package net.slipp.support.security;

import net.slipp.social.security.AutoLoginAuthenticator;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AutoLoginAuthenticatorTest {
	@Autowired
	private AutoLoginAuthenticator dut;
	
	@Test
	public void login() {
		dut.login("javajigi", "test1234");
	}

}
