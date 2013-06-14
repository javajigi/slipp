package net.slipp.web.tag;

import net.slipp.domain.tag.Tag;
import net.slipp.domain.tag.TagInfo;
import net.slipp.domain.user.SocialUser;

public class TagForm {
	private Long tagId;
	
    private String email;
    
    private String name;
    
    private String groupId;
    
    private String description;
    
    public TagForm() {
    }
    
    public TagForm(String name) {
        this.name = name;
    }
    
	public TagForm(Tag tag) {
		TagInfo tagInfo = tag.getTagInfo();
		this.tagId = tag.getTagId();
		this.email = tagInfo.getOwner().getEmail();
		this.name = tag.getName();
		this.groupId = tagInfo.getGroupId();
		this.description = tagInfo.getDescription();
	}
	
	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroupId() {
		return groupId;
	}
    
    public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
    
    public Tag toTag(SocialUser loginUser) {
    	TagInfo tagInfo = new TagInfo(loginUser, getGroupId(), getDescription());
    	return Tag.newTag(getName(), tagInfo);
    }

    @Override
    public String toString() {
        return "TagForm [email=" + email + ", name=" + name + ", description=" + description + "]";
    }
}
