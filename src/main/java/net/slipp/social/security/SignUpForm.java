package net.slipp.social.security;

/**
 * @author javajigi
 * 
 */
public class SignUpForm {
    private String userId;

    private String nickName;

    public SignUpForm() {
    }

    public SignUpForm(String nickName) {
        this(null, nickName);
    }

    public SignUpForm(String userId, String nickName) {
        this.userId = userId;
        this.nickName = nickName;
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

    public void setNickName(String nickname) {
        this.nickName = nickname;
    }

    @Override
    public String toString() {
        return "SignUpForm [userId=" + userId + ", nickname=" + nickName + "]";
    }
}
