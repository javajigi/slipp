package net.slipp.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class UserFormTest {
    @Test
    public void valid_userId() {
        assertUserId("user", true);
        assertUserId("user1234", true);
        assertUserId("usER1234ab", true);
        assertUserId("", false);
        assertUserId("tes", false);
        assertUserId("test1234test23", false);
        assertUserId("테스트", false);
    }
    
    private void assertUserId(String userId, boolean expected) {
        String nickName = "nickName";
        String email = "email@gmail.com";
        
        UserForm form = new UserForm(userId, nickName, email);
        assertThat(form.isValid(), is(expected));
    }

    @Test
    public void valid_nickName() {
        assertNickName("ni", true);
        assertNickName("테스트", true);
        assertNickName("박.재.성", true);
        assertNickName("나만의 닉네임을 가지고", true);
        assertNickName(null, false);
        assertNickName("", false);
        assertNickName("n", false);
        assertNickName("나만의 닉네임을 가지고 테", false);
    }
    
    private void assertNickName(String nickName, boolean expected) {
        String userId = "userId";
        String email = "email@gmail.com";
        
        UserForm form = new UserForm(userId, nickName, email);
        assertThat(form.isValid(), is(expected));
    }
}
