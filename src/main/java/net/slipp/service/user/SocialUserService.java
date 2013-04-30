package net.slipp.service.user;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import net.slipp.domain.user.ExistedUserException;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.user.SocialUserRepository;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Transactional
public class SocialUserService {
    private static final int DEFAULT_RANDOM_PASSWORD_LENGTH = 12;
    
	@Resource(name = "usersConnectionRepository")
	private UsersConnectionRepository usersConnectionRepository;

	@Resource(name = "socialUserRepository")
	private SocialUserRepository socialUserRepository;
	
	@Resource(name = "passwordEncoder")
	private PasswordEncoder passwordEncoder;

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

    public SocialUser createUser(String userId, String userName, String email) {
        String rawPassword = createRawPassword();
        String uuid = UUID.randomUUID().toString();
        SocialUser socialUser = new SocialUser();
        socialUser.setUserId(userId);
        socialUser.setEmail(email);
        socialUser.setProviderId(SocialUser.DEFAULT_SLIPP_PROVIDER_ID);
        socialUser.setProviderUserId(userId);
        socialUser.setRank(1);
        socialUser.setDisplayName(userName);
        socialUser.setAccessToken(uuid);
        socialUser.setRawPassword(rawPassword);
        socialUser.setPassword(encodePassword(rawPassword));
        socialUserRepository.save(socialUser);
        return socialUser;
    }
    
    private String createRawPassword() {
        return RandomStringUtils.randomAlphanumeric(DEFAULT_RANDOM_PASSWORD_LENGTH);
    }

    private String encodePassword(String rawPass) {
        return passwordEncoder.encodePassword(rawPass, null);
    }
}
