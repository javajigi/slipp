package net.slipp.repository.user;

import java.util.List;

import net.slipp.domain.user.SocialUser;

import org.springframework.util.MultiValueMap;

public interface SocialUserRepositoryCustom {
	List<SocialUser> findsByUserIdAndProviderUserIds(String userId, MultiValueMap<String, String> providerUserIds);
}
