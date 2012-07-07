package net.slipp.domain.user;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class SocialUserTest {
	private SocialUser dut;

	@Before
	public void setup() {
		dut = new SocialUser(10);
	}
	
	@Test
	public void isSameUser_null() {
		assertThat(dut.isSameUser(null), is(false));
	}

	@Test
	public void isSameUser_guestUser() {
		assertThat(dut.isSameUser(SocialUser.GUEST_USER), is(false));
	}
	
	@Test
	public void isSameUser_sameUser() {
		SocialUser newUser = new SocialUser(10);
		assertThat(dut.isSameUser(newUser), is(true));
	}
	
	@Test
	public void isSameUser_differentUser() {
		SocialUser newUser = new SocialUser(11);
		assertThat(dut.isSameUser(newUser), is(false));
	}
}
