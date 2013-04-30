package net.slipp.service.user;

import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.user.ExistedUserException;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.user.SocialUserRepository;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.util.Assert;

public class SocialUserService {
	@Resource(name = "usersConnectionRepository")
	private UsersConnectionRepository usersConnectionRepository;

	@Resource(name = "socialUserRepository")
	private SocialUserRepository socialUserRepository;

	public void createNewSocialUser(String userId, Connection<?> connection) throws ExistedUserException {
		Assert.notNull(userId, "userId can't be null!");
		Assert.notNull(connection, "connection can't be null!");

		ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(userId);
		if (!isUserIdAvailable(userId)) {
			throw new ExistedUserException(userId + " already is existed user!");
		}

		connectionRepository.addConnection(connection);
	}

	private boolean isUserIdAvailable(String userId) {
		List<SocialUser> socialUsers = socialUserRepository.findsByUserId(userId);
		return socialUsers.isEmpty();
	}
	
	public SocialUser findById(Long id) {
	    Assert.notNull(id, "id can't be null!");
	    return socialUserRepository.findOne(id);
	}

	public SocialUser findByUserId(String userId) {
		Assert.notNull(userId, "userId can't be null!");
		
		List<SocialUser> socialUsers = socialUserRepository.findsByUserId(userId);
		if (socialUsers.isEmpty()) {
			return null;
		}
		
		return socialUsers.get(0);
	}

	public SocialUser findByUserIdAndConnectionKey(String userId, ConnectionKey connectionKey) {
		Assert.notNull(userId, "userId can't be null!");
		Assert.notNull(connectionKey, "connectionKey can't be null!");
		
		SocialUser socialUser = socialUserRepository.findByUserIdAndProviderIdAndProviderUserId(userId,
				connectionKey.getProviderId(), connectionKey.getProviderUserId());
		return socialUser;
	}
}
