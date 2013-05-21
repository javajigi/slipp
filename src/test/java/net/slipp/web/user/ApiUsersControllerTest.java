package net.slipp.web.user;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import net.slipp.domain.user.SocialUser;

import org.junit.Before;
import org.junit.Test;

public class ApiUsersControllerTest {
	private ApiUsersController dut;
	
	@Before 
	public void setup() {
		dut = new ApiUsersController();
	}
	
	@Test
	public void checkDuplicate_doesnot_existed() {
		String actual = dut.checkDuplicate(SocialUser.GUEST_USER, null);
		assertThat(actual, is("false"));
	}

	@Test
	public void checkDuplicate_login_isSameUser() {
		SocialUser loginUser = new SocialUser(1L);
		SocialUser existedUser = loginUser;
		String actual = dut.checkDuplicate(loginUser, existedUser);
		assertThat(actual, is("false"));
	}
	
	@Test
	public void checkDuplicate_login_isNotSameUser() {
		SocialUser loginUser = new SocialUser(1L);
		SocialUser existedUser = new SocialUser(2L);
		String actual = dut.checkDuplicate(loginUser, existedUser);
		assertThat(actual, is("true"));
	}
}
