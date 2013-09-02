package net.slipp.domain.tag;

import net.slipp.domain.user.SocialUser;

public class TagBuilder {
	private Long id;
	private String name;
	private int taggedCount;
	private boolean pooled;
	private Tag parentTag;
    private SocialUser owner;
    private String groupId;

	public static TagBuilder aTag() {
		return new TagBuilder();
	}
	
	public TagBuilder withId(Long id) {
		this.id = id;
		return this;
	}
	
	public TagBuilder withName(String name) {
		this.name = name;
		return this;
	}
	
	public TagBuilder withPooled(boolean pooled) {
		this.pooled = pooled;
		return this;
	}
	
	public TagBuilder withParentTag(Tag parentTag) {
		this.parentTag = parentTag;
		return this;
	}
	
	public TagBuilder withTaggedCount(int taggedCount) {
		this.taggedCount = taggedCount;
		return this;
	}
	
	public TagBuilder withOwner(SocialUser owner) {
	    this.owner = owner;
        return this;
    }
	
    public TagBuilder withGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }
	
	public Tag build() {
		TagInfo tagInfo = new TagInfo(owner, groupId, null);
		Tag tag = new Tag(name, parentTag, pooled, tagInfo) {
			public Long getTagId() {
				return id;
			}
		};
		
		for (int i = 0; i < taggedCount; i++) {
			tag.tagged();
		}
		
		return tag;
	}
}
