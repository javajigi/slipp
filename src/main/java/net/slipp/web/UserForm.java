package net.slipp.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

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

    public boolean isValid() {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(nickName) || StringUtils.isBlank(email)) {
            return false;
        }
        
        Matcher m = Pattern.compile("[0-9a-zA-Z]{4,12}").matcher(userId);
        if (!m.matches()) {
            return false;
        }
        
        m = Pattern.compile("(.){2,12}").matcher(nickName);
        if (!m.matches()) {
            return false;
        }
        
        m = Pattern.compile("[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})").matcher(email);
        if (!m.matches()) {
            return false;
        }        
        
        return true;
    }
    
    @Override
    public String toString() {
        return "UserForm [userId=" + userId + ", nickName=" + nickName + ", email=" + email + "]";
    }
}
