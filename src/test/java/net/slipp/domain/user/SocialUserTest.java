package net.slipp.domain.user;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

public class SocialUserTest {
    private SocialUser dut;

    private PasswordEncoder encoder = new ShaPasswordEncoder(256);

    @Before
    public void setup() {
        dut = new SocialUser(10);
    }

    @Test
    public void isSameUser_null() {
        assertThat(dut.isSameUser(null), is(false));
    }

    @Test
    public void isSameUser_guestUser() {
        assertThat(dut.isSameUser(SocialUser.GUEST_USER), is(false));
    }

    @Test
    public void isSameUser_sameUser() {
        SocialUser newUser = new SocialUser(10);
        assertThat(dut.isSameUser(newUser), is(true));
    }

    @Test
    public void isSameUser_differentUser() {
        SocialUser newUser = new SocialUser(11);
        assertThat(dut.isSameUser(newUser), is(false));
    }

    @Test
    public void changePassword_이전_비밀번호가_같다() throws Exception {
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        SocialUser socialUser = new SocialUserBuilder().withRawPassword(oldPassword).build();
        socialUser.changePassword(encoder, oldPassword, newPassword);
        assertThat(socialUser.getPassword(), is(encoder.encodePassword(newPassword, null)));
    }
    
    @Test(expected=BadCredentialsException.class)
    public void changePassword_이전_비밀번호가_다르다() throws Exception {
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        SocialUser socialUser = new SocialUserBuilder().withRawPassword(oldPassword).build();
        socialUser.changePassword(encoder, oldPassword + "2", newPassword);
        assertThat(socialUser.getPassword(), is(encoder.encodePassword(newPassword, null)));
    }
}
