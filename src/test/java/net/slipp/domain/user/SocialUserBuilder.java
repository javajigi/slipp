package net.slipp.domain.user;

import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import net.slipp.domain.user.SocialUser;

public class SocialUserBuilder {
    private String userId;

    private String providerId;

    private String providerUserId;
    
    private String accessToken;

    private int rank;

    private String displayName;

    private String profileUrl;

    private String imageUrl;
    
    private String rawPassword;

    private String password;
    
    public static SocialUserBuilder aSocialUser() {
    	return new SocialUserBuilder();
    }

    public SocialUserBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }
    
    public SocialUserBuilder withRawPassword(String rawPassword) {
        PasswordEncoder encoder = new ShaPasswordEncoder(256);
        this.rawPassword = rawPassword;
        this.password = encoder.encodePassword(rawPassword, null);
        return this;
    }
    
    public SocialUserBuilder withAccessToken(String accessToken) {
    	this.accessToken = accessToken;
    	return this;
    }
    
    public SocialUserBuilder withProviderId(String providerId) {
    	this.providerId = providerId;
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
    
    public SocialUser createTestUser(String userId) {
		withUserId(userId);
		withAccessToken("1234-5678" + userId);
		withProviderId("facebook");
		return build();
    }
    
    public SocialUser build() {
        SocialUser socialUser = new SocialUser();
        socialUser.setUserId(userId);
        socialUser.setProviderId(providerId);
        socialUser.setProviderUserId(providerUserId);
        socialUser.setAccessToken(accessToken);
        socialUser.setRank(rank);
        socialUser.setDisplayName(displayName);
        socialUser.setProfileUrl(profileUrl);
        socialUser.setImageUrl(imageUrl);
        socialUser.setPassword(password);
        socialUser.setRawPassword(rawPassword);
        return socialUser;
    }
}
