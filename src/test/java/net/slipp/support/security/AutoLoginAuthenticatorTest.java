package net.slipp.support.security;

import net.slipp.social.security.AutoLoginAuthenticator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-security.xml")
public class AutoLoginAuthenticatorTest {
	@Autowired
	private AutoLoginAuthenticator dut;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	
	@Before
	public void setup() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}
	
	@Test
	public void login() {
		request.addParameter("userId", "javajigi");
		request.addParameter("password", "test1234");
		
		dut.login(request, response);
	}

}
