package net.slipp.service.user;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import net.slipp.domain.user.ExistedUserException;
import net.slipp.domain.user.PasswordDto;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.user.SocialUserRepository;
import net.slipp.service.MailService;

import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Transactional
public class SocialUserService {
    @Resource(name = "usersConnectionRepository")
    private UsersConnectionRepository usersConnectionRepository;

    @Resource(name = "socialUserRepository")
    private SocialUserRepository socialUserRepository;

    @Resource(name = "passwordEncoder")
    PasswordEncoder passwordEncoder;

    @Resource(name = "passwordGenerator")
    private PasswordGenerator passwordGenerator;

    @Resource(name = "mailService")
    private MailService mailService;

    public void createNewSocialUser(String userId, String nickname, Connection<?> connection)
            throws ExistedUserException {
        Assert.notNull(userId, "userId can't be null!");
        Assert.notNull(connection, "connection can't be null!");

        ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(userId);
        if (!isUserIdAvailable(userId)) {
            throw new ExistedUserException(userId + " already is existed user!");
        }

        connectionRepository.addConnection(connection);

        SocialUser socialUser = findByUserId(userId);
        socialUser.setDisplayName(nickname);
    }

    private boolean isUserIdAvailable(String userId) {
        List<SocialUser> socialUsers = socialUserRepository.findsByUserId(userId);
        return socialUsers.isEmpty();
    }

    public SocialUser findById(Long id) {
        Assert.notNull(id, "id can't be null!");
        return socialUserRepository.findOne(id);
    }
    
    public SocialUser findByEmail(Long id) {
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

    public SocialUser createUser(String userId, String email) {
        String rawPassword = passwordGenerator.generate();
        String uuid = UUID.randomUUID().toString();
        SocialUser socialUser = new SocialUser();
        socialUser.setUserId(userId);
        socialUser.setEmail(email);
        socialUser.setProviderId(SocialUser.DEFAULT_SLIPP_PROVIDER_ID);
        socialUser.setProviderUserId(userId);
        socialUser.setRank(1);
        socialUser.setAccessToken(uuid);
        socialUser.setRawPassword(rawPassword);
        socialUser.setPassword(encodePassword(rawPassword));
        socialUserRepository.save(socialUser);
        mailService.sendPasswordInformation(socialUser);
        return socialUser;
    }

    private String encodePassword(String rawPass) {
        return passwordEncoder.encodePassword(rawPass, null);
    }

    public SocialUser changePassword(SocialUser loginUser, PasswordDto password) {
        SocialUser user = socialUserRepository.findOne(password.getId());
        if (user == null) {
            return null;
        }

        user.changePassword(passwordEncoder, password.getOldPassword(), password.getNewPassword());

        return user;
    }
}
