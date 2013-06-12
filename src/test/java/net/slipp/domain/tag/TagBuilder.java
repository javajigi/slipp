package net.slipp.domain.tag;

public class TagBuilder {
	private Long id;
	private String name;
	private int taggedCount;
	private boolean pooled;
	private Tag parentTag;

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
	
	public Tag build() {
		TagInfo tagInfo = new TagInfo(null, null, null);
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
