package net.slipp.web;

public class UserForm {
    private String userId;
    private String nickName;
	private String email;
	
	public UserForm() {
    }
	
    public UserForm(String userId, String nickName, String email) {
        this.userId = userId;
        this.nickName = nickName;
        this.email = email;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "UserForm [userId=" + userId + ", nickName=" + nickName + ", email=" + email + "]";
    }
}
