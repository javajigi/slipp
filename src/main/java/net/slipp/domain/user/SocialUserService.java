package net.slipp.domain.user;

import java.util.List;

import javax.annotation.Resource;

import net.slipp.repository.user.SocialUserRepository;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

public class SocialUserService {
	@Resource(name = "usersConnectionRepository")
	private UsersConnectionRepository usersConnectionRepository;

	@Resource(name = "socialUserRepository")
	private SocialUserRepository socialUserRepository;

	public void createNewSocialUser(String userId, Connection<?> connection) throws ExistedUserException {
		Assert.notNull(userId, "userId can't be null!");
		Assert.notNull(connection, "connection can't be null!");

		ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(userId);
		if (!isUserIdAvailable(userId, connectionRepository)) {
			throw new ExistedUserException(userId + " already is existed user!");
		}

		connectionRepository.addConnection(connection);
	}

	private boolean isUserIdAvailable(String userId, ConnectionRepository connectionRepository) {
		MultiValueMap<String, Connection<?>> connections = findAllConnections(connectionRepository);
		if (connections == null) {
			return true;
		}

		if (connections.isEmpty()) {
			return true;
		}

		return false;
	}

	private MultiValueMap<String, Connection<?>> findAllConnections(ConnectionRepository connectionRepository) {
		return connectionRepository.findAllConnections();
	}

	private MultiValueMap<String, Connection<?>> findAllConnections(String userId) {
		ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(userId);
		return connectionRepository.findAllConnections();
	}

	private Connection<?> findFirstConnection(MultiValueMap<String, Connection<?>> connections) {
		if (connections.isEmpty()) {
			return null;
		}

		for (List<Connection<?>> connectionList : connections.values()) {
			return connectionList.get(0);
		}

		return null;
	}
	
	private ConnectionKey findConnectionKeyByUserId(String userId) {
		MultiValueMap<String, Connection<?>> connections = findAllConnections(userId);
		Connection<?> connection = findFirstConnection(connections);
		ConnectionKey connectionKey = connection.getKey();
		return connectionKey;
	}

	public SocialUser findByUserId(String userId) {
		Assert.notNull(userId, "userId can't be null!");
		
		ConnectionKey connectionKey = findConnectionKeyByUserId(userId);
		return findByUserIdAndConnectionKey(userId, connectionKey);
	}

	public SocialUser findByUserIdAndConnectionKey(String userId, ConnectionKey connectionKey) {
		Assert.notNull(userId, "userId can't be null!");
		Assert.notNull(connectionKey, "connectionKey can't be null!");
		
		SocialUser socialUser = socialUserRepository.findByUserIdAndProviderIdAndProviderUserId(userId,
				connectionKey.getProviderId(), connectionKey.getProviderUserId());
		return socialUser;
	}
}
