package net.slipp.social.security;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class SlippUserDetailsServiceTest {

	@Test
	public void isAdmin() {
		String adminUsers = "javajigi:sanjigi";
		SlippUserDetailsService dut = new SlippUserDetailsService();
		dut.setAdminUsers(adminUsers);
		assertThat(dut.isAdmin("javajigi"), is(true));
		
		assertThat(dut.isAdmin("userId"), is(false));
	}

}
