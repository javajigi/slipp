package net.slipp.service.user;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import net.slipp.domain.user.ExistedUserException;
import net.slipp.service.user.SocialUserService;

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
	private Connection<?> connection;
	
	@InjectMocks
	private SocialUserService dut = new SocialUserService();
	
	@Test
	public void createNewSocialUser_availableUserId() throws Exception {
		String userId = "userId";
		when(usersConnectionRepository.createConnectionRepository(userId)).thenReturn(connectionRepository);
		MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
		when(connectionRepository.findAllConnections()).thenReturn(connections);
		
		dut.createNewSocialUser(userId, connection);
		
		verify(connectionRepository).addConnection(connection);
	}
	
	@Test(expected=ExistedUserException.class)
	public void createNewSocialUser_notAvailableUserId() throws Exception {
		String userId = "userId";
		when(usersConnectionRepository.createConnectionRepository(userId)).thenReturn(connectionRepository);
		MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
		connections.add("facebook", connection);
		when(connectionRepository.findAllConnections()).thenReturn(connections);
		
		dut.createNewSocialUser(userId, connection);
	}
}
