package net.slipp.domain.tag;

public class TagBuilder {
	private String name;
	private int taggedCount;
	private boolean pooled;
	private Tag parentTag;

	public static TagBuilder aTag() {
		return new TagBuilder();
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
		Tag tag = new Tag(name, parentTag, pooled);
		for (int i = 0; i < taggedCount; i++) {
			tag.tagged();
		}
		
		return tag;
	}
}
