package net.slipp.domain.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import net.slipp.domain.ProviderType;
import net.slipp.support.utils.MD5Util;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.encoding.PasswordEncoder;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "userId", "providerId", "providerUserId" }),
        @UniqueConstraint(columnNames = { "userId", "providerId", "rank" }) })
@Cache(region = "socialUser", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SocialUser {
    public static final SocialUser GUEST_USER = new GuestSocialUser();
    
    public static final String DEFAULT_SLIPP_USER_PROFILE_SUFFIX = "http://www.gravatar.com/avatar/";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * A local identifier for the user, in our case the username.
     */
    private String userId;

    private String password;
    
    @Transient
    private String rawPassword;

    private String email;

    @Column(nullable=false, columnDefinition = ProviderType.COLUMN_DEFINITION)
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
    
    private boolean blocked = false;

    public SocialUser() {
    }

    public SocialUser(long id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProviderId() {
        return providerId;
    }
    
    public ProviderType getProviderIdBySnsType() {
        return ProviderType.valueOf(providerId);
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

    public boolean isSameUser(SocialUser socialUser) {
        if (socialUser == null) {
            return false;
        }

        return this.id == socialUser.id;
    }

    public boolean isFacebookUser() {
        return ProviderType.facebook == getProviderIdBySnsType();
    }

    public boolean isSLiPPUser() {
        return ProviderType.slipp == getProviderIdBySnsType();
    }
    
    public boolean isBlocked() {
		return blocked;
	}
    
    public void blocked() {
    	this.blocked = true;
    }
    
    public void changePassword(PasswordEncoder encoder, String oldPassword, String newPassword) {
        String oldEncodedPassword = encoder.encodePassword(oldPassword, null);
        if (!password.equals(oldEncodedPassword)) {
            throw new BadCredentialsException("현재 비밀번호가 다릅니다.");
        }
        
        this.password = encoder.encodePassword(newPassword, null);
    }
    
	public void resetPassword(PasswordEncoder encoder, String newPassword) {
		this.rawPassword = newPassword;
		this.password = encoder.encodePassword(newPassword, null);
	}
    
	public void update(String email, String userId) {
		this.email = email;
		this.userId = userId;
		
		if (isSLiPPUser()) {
			this.imageUrl = DEFAULT_SLIPP_USER_PROFILE_SUFFIX + MD5Util.md5Hex(email);
		}
	}
	
    static class GuestSocialUser extends SocialUser {
        @Override
        public boolean isGuest() {
            return true;
        }
    }
    
    public String getUrl() {
    	return String.format("/users/%d/%s", id, userId);
    }
    
    @Override
    public String toString() {
        return "SocialUser [id=" + id + ", userId=" + userId + ", providerId=" + providerId + ", providerUserId="
                + providerUserId + ", rank=" + rank + ", displayName=" + displayName + ", profileUrl=" + profileUrl
                + ", imageUrl=" + imageUrl + ", accessToken=" + accessToken + ", secret=" + secret + ", refreshToken="
                + refreshToken + ", expireTime=" + expireTime + ", createDate=" + createDate + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accessToken == null) ? 0 : accessToken.hashCode());
        result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        result = prime * result + ((expireTime == null) ? 0 : expireTime.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
        result = prime * result + ((profileUrl == null) ? 0 : profileUrl.hashCode());
        result = prime * result + ((providerId == null) ? 0 : providerId.hashCode());
        result = prime * result + ((providerUserId == null) ? 0 : providerUserId.hashCode());
        result = prime * result + rank;
        result = prime * result + ((refreshToken == null) ? 0 : refreshToken.hashCode());
        result = prime * result + ((secret == null) ? 0 : secret.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SocialUser other = (SocialUser) obj;
        if (accessToken == null) {
            if (other.accessToken != null)
                return false;
        } else if (!accessToken.equals(other.accessToken))
            return false;
        if (createDate == null) {
            if (other.createDate != null)
                return false;
        } else if (!createDate.equals(other.createDate))
            return false;
        if (displayName == null) {
            if (other.displayName != null)
                return false;
        } else if (!displayName.equals(other.displayName))
            return false;
        if (expireTime == null) {
            if (other.expireTime != null)
                return false;
        } else if (!expireTime.equals(other.expireTime))
            return false;
        if (id != other.id)
            return false;
        if (imageUrl == null) {
            if (other.imageUrl != null)
                return false;
        } else if (!imageUrl.equals(other.imageUrl))
            return false;
        if (profileUrl == null) {
            if (other.profileUrl != null)
                return false;
        } else if (!profileUrl.equals(other.profileUrl))
            return false;
        if (providerId == null) {
            if (other.providerId != null)
                return false;
        } else if (!providerId.equals(other.providerId))
            return false;
        if (providerUserId == null) {
            if (other.providerUserId != null)
                return false;
        } else if (!providerUserId.equals(other.providerUserId))
            return false;
        if (rank != other.rank)
            return false;
        if (refreshToken == null) {
            if (other.refreshToken != null)
                return false;
        } else if (!refreshToken.equals(other.refreshToken))
            return false;
        if (secret == null) {
            if (other.secret != null)
                return false;
        } else if (!secret.equals(other.secret))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }
}
