package net.slipp.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class UserForm {
    private String email;
    private String userId;
	
	public UserForm() {
    }
	
    public UserForm(String userId, String email) {
        this.userId = userId;
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

    public boolean isValid() {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(email)) {
            return false;
        }
        
        Matcher m = Pattern.compile("(.){2,12}").matcher(userId);
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
        return "UserForm [userId=" + userId + ", email=" + email + "]";
    }
}
