package net.slipp.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class UserFormTest {
    @Test
    public void valid_userId() {
        assertUserId("ni", true);
        assertUserId("테스트", true);
        assertUserId("박.재.성", true);
        assertUserId("나만의 닉네임을 가지고", true);
        assertUserId(null, false);
        assertUserId("", false);
        assertUserId("n", false);
        assertUserId("나만의 닉네임을 가지고 테", false);
    }
    
    private void assertUserId(String userId, boolean expected) {
        String email = "email@gmail.com";
        
        UserForm form = new UserForm(userId, email);
        assertThat(form.isValid(), is(expected));
    }
}
