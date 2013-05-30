package net.slipp.domain.user;

import net.slipp.domain.ProviderType;
import net.slipp.user.MockPasswordEncoder;

public class SocialUserBuilder {
    private String userId;

    private String providerUserId;
    
    private String accessToken;

    private int rank;

    private String displayName;

    private String profileUrl;

    private String imageUrl;
    
    private String rawPassword;

    private String password;

    private String email;

    private ProviderType providerType = ProviderType.facebook;
    
    public static SocialUserBuilder aSocialUser() {
    	return new SocialUserBuilder();
    }

    public SocialUserBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }
    
    public SocialUserBuilder withRawPassword(String rawPassword) {
        MockPasswordEncoder encoder = new MockPasswordEncoder();
        this.rawPassword = rawPassword;
        this.password = encoder.encodePassword(rawPassword);
        return this;
    }
    
    public SocialUserBuilder withAccessToken(String accessToken) {
    	this.accessToken = accessToken;
    	return this;
    }
    
    public SocialUserBuilder withProviderType(ProviderType providerType) {
    	this.providerType = providerType;
    	return this;
    }
    
    public SocialUserBuilder withDisplayName(String displayName) {
    	this.displayName = displayName;
    	return this;
    }
    
    public SocialUserBuilder withRank(int rank) {
    	this.rank = rank;
    	return this;
    }
    
    public SocialUserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }
    
    public SocialUser createTestUser(String userId) {
		withUserId(userId);
		withAccessToken("1234-5678" + userId);
		withProviderType(ProviderType.facebook);
		return build();
    }
    
    public SocialUser build() {
        SocialUser socialUser = new SocialUser();
        socialUser.setUserId(userId);
        socialUser.setProviderId(providerType.name());
        socialUser.setProviderUserId(providerUserId);
        socialUser.setAccessToken(accessToken);
        socialUser.setRank(rank);
        socialUser.setDisplayName(displayName);
        socialUser.setProfileUrl(profileUrl);
        socialUser.setImageUrl(imageUrl);
        socialUser.setPassword(password);
        socialUser.setRawPassword(rawPassword);
        socialUser.setEmail(email);
        return socialUser;
    }
}
