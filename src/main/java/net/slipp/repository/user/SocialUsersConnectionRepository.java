package net.slipp.repository.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.slipp.domain.user.SocialUser;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;

public class SocialUsersConnectionRepository implements UsersConnectionRepository {
	private SocialUserRepository socialUserRepository;

	private final ConnectionFactoryLocator connectionFactoryLocator;

	private final TextEncryptor textEncryptor;

	private ConnectionSignUp connectionSignUp;
	
	public SocialUsersConnectionRepository(SocialUserRepository socialUserRepository, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {
		this.socialUserRepository = socialUserRepository;
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
	}
	
	public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
		this.connectionSignUp = connectionSignUp;
	}
	
	@Override
	public List<String> findUserIdsWithConnection(Connection<?> connection) {
		List<String> usrs = new ArrayList<String>();
		ConnectionKey key = connection.getKey();
		List<SocialUser> users = socialUserRepository.findsByProviderIdAndProviderUserId(key.getProviderId(), key.getProviderUserId());
		if(!users.isEmpty()){
			for(SocialUser user : users){
				usrs.add(user.getUserId());
			}
			return usrs;
		}

		if (connectionSignUp != null) {
			String newUserId = connectionSignUp.execute(connection);
			if(newUserId == null)
				//auto signup failed, so we need to go to a sign up form
				return usrs;
			createConnectionRepository(newUserId).addConnection(connection);
			usrs.add(newUserId);
		}
		//if empty we should go to the sign up form
		return usrs;
	}

	@Override
	public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
		List<String> userIds = socialUserRepository.findUsersConnectedTo(providerId, providerUserIds);
		return new HashSet<String>(userIds);
	}

	@Override
	public ConnectionRepository createConnectionRepository(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("userId cannot be null");
		}
		return new JpaConnectionRepository(userId, socialUserRepository, connectionFactoryLocator, textEncryptor);
	}

}
