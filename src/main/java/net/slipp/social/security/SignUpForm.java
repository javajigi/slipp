package net.slipp.social.security;

/**
 * @author javajigi
 *
 */
public class SignUpForm {
	private String username;
	
	public SignUpForm() {
	}

	public SignUpForm(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "SignUpForm [username=" + username + "]";
	}
}
