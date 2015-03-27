package net.slipp.domain.tag;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.StringUtils;

@Embeddable
public class TagInfo {
    static final String FACEBOOK_GROUP_URL_PREFIX = "https://www.facebook.com/groups/";
    
    @Column(length=100, nullable=true)
    private String groupId;
    
    @Column(length=1000, nullable=true)
    private String description;
    
    public TagInfo() {
	}
	
	public TagInfo(String groupId, String description) {
		this.groupId = groupId;
		this.description = description;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getDescription() {
		return description;
	}
	
    public boolean isConnectGroup() {
        return !StringUtils.isBlank(groupId);
    }
    
    public String getGroupUrl() {
        if (isConnectGroup()) {
            return FACEBOOK_GROUP_URL_PREFIX + groupId;
        }
        
        return "";
    }

    @Override
    public String toString() {
        return "TagInfo [groupId=" + groupId + ", description=" + description + "]";
    }
}
