package net.slipp.service.user;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.slipp.domain.user.ExistedUserException;
import net.slipp.domain.user.PasswordDto;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.user.SocialUserRepository;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
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
	
	private PasswordEncoder encoder = new ShaPasswordEncoder(256);
	
	@Mock
	private Connection<?> connection;
	
	@InjectMocks
	private SocialUserService dut = new SocialUserService();
	
	@Before
	public void setup() {
	    dut.passwordEncoder = encoder;
	}
	
	@Ignore @Test
	public void createNewSocialUser_availableUserId() throws Exception {
		String userId = "userId";
		when(usersConnectionRepository.createConnectionRepository(userId)).thenReturn(connectionRepository);
		MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
		when(connectionRepository.findAllConnections()).thenReturn(connections);
		
		dut.createNewSocialUser(userId, connection);
		
		verify(connectionRepository).addConnection(connection);
	}
	
	@Ignore @Test(expected=ExistedUserException.class)
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
	    SocialUser loginUser = new SocialUser(1L);
	    PasswordDto password = new PasswordDto(loginUser.getId(), "oldPassword", "newPassword", "newPassword");
	    when(socialUserRepository.findOne(loginUser.getId())).thenReturn(loginUser);
	    
        SocialUser changedUser = dut.changePassword(loginUser, password);
        assertThat(changedUser.getPassword(), is(encoder.encodePassword("newPassword", null)));
    }
}
