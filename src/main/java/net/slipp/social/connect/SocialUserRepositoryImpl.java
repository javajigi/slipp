package net.slipp.social.connect;

import java.util.List;

import org.springframework.util.MultiValueMap;

public class SocialUserRepositoryImpl implements SocialUserRepositoryCustom {
	@Override
	public List<SocialUser> findsByUserIdAndProviderUserIds(String userId, MultiValueMap<String, String> providerUserIds) {
		throw new UnsupportedOperationException("findsByUserIdAndProviderUserIds method not supported!");
	}
}
