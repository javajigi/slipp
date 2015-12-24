package net.slipp.service.user;

import com.google.common.collect.Lists;
import net.slipp.domain.user.ExistedUserException;
import net.slipp.domain.user.PasswordDto;
import net.slipp.domain.user.SocialUser;
import net.slipp.domain.user.SocialUserBuilder;
import net.slipp.repository.user.SocialUserRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SocialUserServiceTest {
    @Mock
    private UsersConnectionRepository usersConnectionRepository;

    @Mock
    private ConnectionRepository connectionRepository;

    @Mock
    private SocialUserRepository socialUserRepository;

    private BCryptPasswordEncoder encoder;

    @Mock
    private Connection<?> connection;

    @InjectMocks
    private SocialUserService dut = new SocialUserService();

    @Before
    public void setup() {
        encoder = new BCryptPasswordEncoder();
        dut.passwordEncoder = encoder;
    }

    @Ignore
    @Test
    public void createNewSocialUser_availableUserId() throws Exception {
        String userId = "userId";
        when(usersConnectionRepository.createConnectionRepository(userId)).thenReturn(connectionRepository);
        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        when(connectionRepository.findAllConnections()).thenReturn(connections);

        dut.createNewSocialUser(userId, connection);

        verify(connectionRepository).addConnection(connection);
    }

    @Ignore
    @Test(expected = ExistedUserException.class)
    public void createNewSocialUser_notAvailableUserId() throws Exception {
        String userId = "userId";
        when(usersConnectionRepository.createConnectionRepository(userId)).thenReturn(connectionRepository);
        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        connections.add("facebook", connection);
        when(connectionRepository.findAllConnections()).thenReturn(connections);

        dut.createNewSocialUser(userId, connection);
    }

    @Test
    public void changePassword() throws Exception {
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        SocialUser socialUser = new SocialUserBuilder().withRawPassword(oldPassword).build();
        PasswordDto password = new PasswordDto(socialUser.getId(), oldPassword, newPassword, newPassword);
        when(socialUserRepository.findOne(socialUser.getId())).thenReturn(socialUser);

        SocialUser changedUser = dut.changePassword(socialUser, password);
        assertThat(changedUser.getPassword(), is(encoder.encode(newPassword)));
    }

    @Test
    public void updateSlippUser_isSameEmail() throws Exception {
        String email = "javajigi@slipp.net";
        SocialUser socialUser = createUser(1L, email, "userId");
        when(socialUserRepository.findOne(socialUser.getId())).thenReturn(socialUser);

        String updateUserId = "updateUserId";
        SocialUser updateSocialUser = dut.updateSlippUser(socialUser, email, updateUserId);
        assertThat(updateSocialUser.getUserId(), is(updateUserId));
    }

    @Test
    public void updateSlippUser_isNotExistedAnotherEmail() throws Exception {
        String email = "javajigi@slipp.net";
        SocialUser socialUser = createUser(1L, email, "userId");
        String anotherEmail = email + "2";
        when(socialUserRepository.findOne(socialUser.getId())).thenReturn(socialUser);
        when(socialUserRepository.findByEmail(anotherEmail)).thenReturn(null);

        String updateUserId = "updateUserId";
        SocialUser updateSocialUser = dut.updateSlippUser(socialUser, anotherEmail, updateUserId);
        assertThat(updateSocialUser.getUserId(), is(updateUserId));

        verify(socialUserRepository).findByEmail(anotherEmail);
    }

    @Test(expected = ExistedUserException.class)
    public void updateSlippUser_isExistedAnotherEmail() throws Exception {
        String email = "javajigi@slipp.net";
        SocialUser socialUser = createUser(1L, email, "userId");
        String anotherEmail = email + "2";
        when(socialUserRepository.findOne(socialUser.getId())).thenReturn(socialUser);

        SocialUser anotherUser = createUser(2L, email + 2, "userId");
        when(socialUserRepository.findByEmail(anotherEmail)).thenReturn(anotherUser);

        String updateUserId = "updateUserId";
        dut.updateSlippUser(socialUser, anotherEmail, updateUserId);
    }

    @Test
    public void updateSlippUser_isNotExistedAnotherUserId() throws Exception {
        String email = "javajigi@slipp.net";
        String userId = "userId";
        SocialUser socialUser = createUser(1L, email, userId);
        String anotherUserId = userId + "2";
        when(socialUserRepository.findOne(socialUser.getId())).thenReturn(socialUser);
        when(socialUserRepository.findByEmail(email)).thenReturn(null);
        List<SocialUser> users = Lists.newArrayList();
        when(socialUserRepository.findsByUserId(anotherUserId)).thenReturn(users);

        SocialUser updateSocialUser = dut.updateSlippUser(socialUser, email, anotherUserId);
        assertThat(updateSocialUser.getUserId(), is(anotherUserId));

        verify(socialUserRepository).findByEmail(email);
        verify(socialUserRepository).findsByUserId(anotherUserId);
    }
    
    @Test(expected = ExistedUserException.class)
    public void updateSlippUser_isExistedAnotherUserId() throws Exception {
        String email = "javajigi@slipp.net";
        String userId = "userId";
        SocialUser socialUser = createUser(1L, email, userId);
        String anotherUserId = userId + "2";
        when(socialUserRepository.findOne(socialUser.getId())).thenReturn(socialUser);
        when(socialUserRepository.findByEmail(email)).thenReturn(null);
        List<SocialUser> users = Lists.newArrayList(createUser(2L, email, anotherUserId));
        when(socialUserRepository.findsByUserId(anotherUserId)).thenReturn(users);

        dut.updateSlippUser(socialUser, email, anotherUserId);
    }

    private SocialUser createUser(long id, String email, String nickName) {
        SocialUser socialUser = new SocialUserBuilder().withId(id).withEmail(email).withUserId(nickName).build();
        return socialUser;
    }
}
