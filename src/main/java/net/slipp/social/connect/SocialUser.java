package net.slipp.social.connect;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = { "userId", "providerId", "providerUserId" }),
        @UniqueConstraint(columnNames = { "userId", "providerId", "rank" })})
public class SocialUser {
    public static final SocialUser GUEST_USER = new GuestSocialUser();
    
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * A local identifier for the user, in our case the username.
	 */
	private String userId;

	@Column(nullable = false)
	private String providerId;

	private String providerUserId;

	@Column(nullable = false)
	private int rank;

	private String displayName;

	private String profileUrl;

	private String imageUrl;

	@Column(nullable = false)
	private String accessToken;

	private String secret;

	private String refreshToken;
	
	private Long expireTime;

	private Date createDate = new Date();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

    public boolean isGuest() {
        return false;
    }
    
    static class GuestSocialUser extends SocialUser {
        @Override
        public boolean isGuest() {
            return true;
        }
    }

    @Override
    public String toString() {
        return "SocialUser [id=" + id + ", userId=" + userId + ", providerId="
                + providerId + ", providerUserId=" + providerUserId + ", rank="
                + rank + ", displayName=" + displayName + ", profileUrl="
                + profileUrl + ", imageUrl=" + imageUrl + ", accessToken="
                + accessToken + ", secret=" + secret + ", refreshToken="
                + refreshToken + ", expireTime=" + expireTime + ", createDate="
                + createDate + "]";
    }
}
