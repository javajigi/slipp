package net.slipp.social.security;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class SignUpFormTest {
	@Test
	public void getUsername() {
		String username = "username";
		SignUpForm signUpForm = new SignUpForm(username);
		assertThat(signUpForm.getUsername(), is(username));
	}
}
