package net.slipp.domain.tag;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import net.slipp.domain.user.SocialUser;

@Embeddable
public class TagInfo {
    @ManyToOne
    @org.hibernate.annotations.ForeignKey(name = "fk_tag_owner")
    private SocialUser owner;
    
    @Column(length=100, nullable=true)
    private String groupId;
    
    @Column(length=1000, nullable=true)
    private String description;
    
    public TagInfo() {
	}
	
	public TagInfo(SocialUser loginUser, String groupId, String description) {
		this.owner = loginUser;
		this.groupId = groupId;
		this.description = description;
	}

	public SocialUser getOwner() {
		return owner;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getDescription() {
		return description;
	}
	
	public boolean isRequestedTag() {
	    if (owner == null) {
	        return false;
	    }
	    return true;
	}

	@Override
	public String toString() {
		return "TagInfo [owner=" + owner + ", groupId=" + groupId + ", description="
				+ description + "]";
	}
}
