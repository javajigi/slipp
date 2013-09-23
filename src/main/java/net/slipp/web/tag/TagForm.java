package net.slipp.web.tag;

import net.slipp.domain.tag.Tag;
import net.slipp.domain.tag.TagInfo;

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
    
    @Override
    public String toString() {
        return "TagForm [email=" + email + ", name=" + name + ", description=" + description + "]";
    }
}
