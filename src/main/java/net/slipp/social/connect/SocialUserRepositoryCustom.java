package net.slipp.social.connect;

import java.util.List;

import org.springframework.util.MultiValueMap;

public interface SocialUserRepositoryCustom {
	List<SocialUser> findsByUserIdAndProviderUserIds(String userId, MultiValueMap<String, String> providerUserIds);
}
