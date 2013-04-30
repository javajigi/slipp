package net.slipp.repository.user;

import java.util.ArrayList;
import java.util.List;

import net.slipp.domain.user.SocialUser;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.NoSuchConnectionException;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class JpaConnectionRepository implements ConnectionRepository {
	private SocialUserRepository socialUserRepository;

	private final String userId;

	private final ConnectionFactoryLocator connectionFactoryLocator;

	private final TextEncryptor textEncryptor;

	public JpaConnectionRepository(String userId, SocialUserRepository socialUserRepository,
			ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {
		this.socialUserRepository = socialUserRepository;
		this.userId = userId;
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
	}

	@Override
	public MultiValueMap<String, Connection<?>> findAllConnections() {
		List<Connection<?>> resultList = connectionMapper.mapEntities(socialUserRepository.findsByUserId(userId));

		MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
		for (Connection<?> connection : resultList) {
			String providerId = connection.getKey().getProviderId();
			connections.add(providerId, connection);
		}
		return connections;
	}

	@Override
	public List<Connection<?>> findConnections(String providerId) {
		return connectionMapper.mapEntities(socialUserRepository.findsByUserIdAndProviderId(userId, providerId));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A> List<Connection<A>> findConnections(Class<A> apiType) {
		List<?> connections = findConnections(getProviderId(apiType));
		return (List<Connection<A>>) connections;
	}

	@Override
	public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUserIds) {
		if (providerUserIds.isEmpty()) {
			throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
		}

		List<Connection<?>> resultList = connectionMapper.mapEntities(socialUserRepository
				.findsByUserIdAndProviderUserIds(userId, providerUserIds));

		MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<String, Connection<?>>();
		for (Connection<?> connection : resultList) {
			String providerId = connection.getKey().getProviderId();
			List<String> userIds = providerUserIds.get(providerId);
			List<Connection<?>> connections = connectionsForUsers.get(providerId);
			if (connections == null) {
				connections = new ArrayList<Connection<?>>(userIds.size());
				for (int i = 0; i < userIds.size(); i++) {
					connections.add(null);
				}
				connectionsForUsers.put(providerId, connections);
			}
			String providerUserId = connection.getKey().getProviderUserId();
			int connectionIndex = userIds.indexOf(providerUserId);
			connections.set(connectionIndex, connection);
		}
		return connectionsForUsers;
	}

	@Override
	public Connection<?> getConnection(ConnectionKey connectionKey) {
		try {
			return connectionMapper.mapEntity(socialUserRepository.findByUserIdAndProviderIdAndProviderUserId(userId,
					connectionKey.getProviderId(), connectionKey.getProviderUserId()));
		} catch (EmptyResultDataAccessException e) {
			throw new NoSuchConnectionException(connectionKey);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
		String providerId = getProviderId(apiType);
		return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
		String providerId = getProviderId(apiType);
		Connection<A> connection = (Connection<A>) findPrimaryConnection(providerId);
		if (connection == null) {
			throw new NotConnectedException(providerId);
		}
		return connection;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
		String providerId = getProviderId(apiType);
		return (Connection<A>) findPrimaryConnection(providerId);
	}
	
	private Connection<?> findPrimaryConnection(String providerId) {
		List<Connection<?>> connections = connectionMapper.mapEntities(socialUserRepository.findsPrimary(userId, providerId));
		if (connections.size() > 0) {
			return connections.get(0);
		} else {
			return null;
		}		
	}

	@Override
	public void addConnection(Connection<?> connection) {
		try {
			ConnectionData data = connection.createData();
			int rank = getRank(data.getProviderId()) ;
			SocialUser socialUser = new SocialUser();
			socialUser.setUserId(userId);
			socialUser.setProviderId(data.getProviderId());
			socialUser.setProviderUserId(data.getProviderUserId());
			socialUser.setRank(rank);
			socialUser.setDisplayName(data.getDisplayName());
			socialUser.setProfileUrl(data.getProfileUrl());
			socialUser.setImageUrl(data.getImageUrl());
			socialUser.setAccessToken(encrypt(data.getAccessToken()));
			socialUser.setSecret(encrypt(data.getSecret()));
			socialUser.setRefreshToken(encrypt(data.getRefreshToken()));
			socialUser.setExpireTime(data.getExpireTime());
			socialUserRepository.save(socialUser);
		} catch (DuplicateKeyException e) {
			throw new DuplicateConnectionException(connection.getKey());
		}
	}

	private int getRank(String providerId) {
		List<Integer> result = socialUserRepository.findsRank(userId, providerId);
		if (result.isEmpty() || result.get(0) == null)
			return 1;
		return result.get(0) + 1;
	}

	@Override
	public void updateConnection(Connection<?> connection) {
		ConnectionData data = connection.createData();
		
		SocialUser su = socialUserRepository.findByUserIdAndProviderIdAndProviderUserId(userId, data.getProviderId(),data.getProviderUserId());
		if(su != null){
			su.setDisplayName(data.getDisplayName());
			su.setProfileUrl(data.getProfileUrl());
			su.setImageUrl(data.getImageUrl());
			su.setAccessToken(encrypt(data.getAccessToken()));
			su.setSecret(encrypt(data.getSecret()));
			su.setRefreshToken(encrypt(data.getRefreshToken()));
			su.setExpireTime(data.getExpireTime());
			
			socialUserRepository.save(su);
		}
	}

	@Override
	public void removeConnections(String providerId) {
		SocialUser socialUser = socialUserRepository.findByUserIdAndProviderId(userId, providerId);
		socialUserRepository.delete(socialUser);
	}

	@Override
	public void removeConnection(ConnectionKey connectionKey) {
		SocialUser socialUser = socialUserRepository.findByUserIdAndProviderIdAndProviderUserId(userId, connectionKey.getProviderId(), connectionKey.getProviderUserId());
		socialUserRepository.delete(socialUser);
	}

	private final ServiceProviderConnectionMapper connectionMapper = new ServiceProviderConnectionMapper();

	private final class ServiceProviderConnectionMapper {
		public List<Connection<?>> mapEntities(List<SocialUser> socialUsers) {
			List<Connection<?>> result = new ArrayList<Connection<?>>();
			for (SocialUser su : socialUsers) {
				result.add(mapEntity(su));
			}
			return result;
		}

		public Connection<?> mapEntity(SocialUser socialUser) {
			ConnectionData connectionData = mapConnectionData(socialUser);
			ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionData
					.getProviderId());
			return connectionFactory.createConnection(connectionData);
		}

		private ConnectionData mapConnectionData(SocialUser socialUser) {
			return new ConnectionData(socialUser.getProviderId(), socialUser.getProviderUserId(),
					socialUser.getDisplayName(), socialUser.getProfileUrl(), socialUser.getImageUrl(),
					decrypt(socialUser.getAccessToken()), decrypt(socialUser.getSecret()),
					decrypt(socialUser.getRefreshToken()), expireTime(socialUser.getExpireTime()));
		}

		private String decrypt(String encryptedText) {
			return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText;
		}

		private Long expireTime(Long expireTime) {
			return expireTime == null || expireTime == 0 ? null : expireTime;
		}

	}

	private <A> String getProviderId(Class<A> apiType) {
		return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
	}

	private String encrypt(String text) {
		return text != null ? textEncryptor.encrypt(text) : text;
	}
}
