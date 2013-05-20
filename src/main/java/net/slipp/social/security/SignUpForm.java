package net.slipp.social.security;

/**
 * @author javajigi
 * 
 */
public class SignUpForm {
    private String userId;

    public SignUpForm() {
    }

    public SignUpForm(String userId) {
        this.userId = userId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SignUpForm [userId=" + userId + "]";
    }
}
