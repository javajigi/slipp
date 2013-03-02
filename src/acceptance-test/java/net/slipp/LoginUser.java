package net.slipp;

public class LoginUser {
	private String email;
	private String password;
	private String nickName;

	public LoginUser(String email, String password, String nickName) {
		this.email = email;
		this.password = password;
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getNickName() {
		return nickName;
	}

	@Override
	public String toString() {
		return "LoginUser [email=" + email + ", password=" + password + ", nickName=" + nickName + "]";
	}
}
