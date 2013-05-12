package net.slipp.service.user;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.slipp.domain.user.ExistedUserException;
import net.slipp.domain.user.PasswordDto;
import net.slipp.domain.user.SocialUser;
import net.slipp.domain.user.SocialUserBuilder;
import net.slipp.repository.user.SocialUserRepository;
import net.slipp.user.MockPasswordEncoder;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(MockitoJUnitRunner.class)
public class SocialUserServiceTest {
	@Mock
	private UsersConnectionRepository usersConnectionRepository;
	
	@Mock
	private ConnectionRepository connectionRepository;
	
	@Mock
	private SocialUserRepository socialUserRepository;
	
	private MockPasswordEncoder encoder;
	
	@Mock
	private Connection<?> connection;
	
	@InjectMocks
	private SocialUserService dut = new SocialUserService();
	
	@Before
	public void setup() {
	    encoder = new MockPasswordEncoder();
	    dut.passwordEncoder = encoder;
	}
	
	@Ignore @Test
	public void createNewSocialUser_availableUserId() throws Exception {
		String userId = "userId";
		String nickName = "nickName";
		when(usersConnectionRepository.createConnectionRepository(userId)).thenReturn(connectionRepository);
		MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
		when(connectionRepository.findAllConnections()).thenReturn(connections);
		
		dut.createNewSocialUser(userId, nickName, connection);
		
		verify(connectionRepository).addConnection(connection);
	}
	
	@Ignore @Test(expected=ExistedUserException.class)
	public void createNewSocialUser_notAvailableUserId() throws Exception {
		String userId = "userId";
		String nickName = "nickName";
		when(usersConnectionRepository.createConnectionRepository(userId)).thenReturn(connectionRepository);
		MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
		connections.add("facebook", connection);
		when(connectionRepository.findAllConnections()).thenReturn(connections);
		
		dut.createNewSocialUser(userId, nickName, connection);
	}
	
	@Test
    public void changePassword() throws Exception {
	    String oldPassword = "oldPassword";
	    String newPassword = "newPassword";
	    
	    SocialUser socialUser = new SocialUserBuilder().withRawPassword(oldPassword).build();
        PasswordDto password = new PasswordDto(socialUser.getId(), oldPassword, newPassword, newPassword);
	    when(socialUserRepository.findOne(socialUser.getId())).thenReturn(socialUser);
	    
        SocialUser changedUser = dut.changePassword(socialUser, password);
        assertThat(changedUser.getPassword(), is(encoder.encodePassword(newPassword, null)));
    }
}
