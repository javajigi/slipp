package net.slipp.domain.tag;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import net.slipp.domain.qna.Question;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.google.common.collect.Sets;

@Entity
@Cache(region = "tag", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long tagId;

	@Column(name = "name", length = 50, unique = true, nullable = false)
	private String name;

	private int taggedCount = 0;

	@Column(name = "pooled", nullable = false)
	private boolean pooled;

	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name = "fk_tag_parent_id"))
	private Tag parent;

	@Embedded
	private TagInfo tagInfo;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
	private Set<Question> questions = Sets.newHashSet();

	public Tag() {
	}

	Tag(String name, Tag parent, TagInfo tagInfo) {
		this.name = name;
		this.parent = parent;
		this.pooled = true;
		this.tagInfo = tagInfo;
	}

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTaggedCount(int taggedCount) {
		this.taggedCount = taggedCount;
	}

	public int getTaggedCount() {
		return taggedCount;
	}

	public boolean isPooled() {
		return pooled;
	}
	
	public boolean isTagged() {
		return taggedCount > 0;
	}

	public void tagged() {
		taggedCount += 1;
	}

	public void deTagged() {
		taggedCount -= 1;
	}

	public Tag getParent() {
		return this.parent;
	}

	public TagInfo getTagInfo() {
		return tagInfo;
	}
	
	public String getGroupId() {
		if (tagInfo == null) {
			return null;
		}
		return tagInfo.getGroupId();
	}

	private boolean isRootTag() {
		return parent == null;
	}

	public void movePooled(Tag parent) {
		this.pooled = true;
		this.parent = parent;

		for (Question each : questions) {
			each.tagsToDenormalizedTags();
		}
	}
	
    public void moveGroupTag(String groupId) {
        this.pooled = true;
        this.tagInfo = new TagInfo(groupId, this.name);
    }

	public boolean isConnectGroup() {
	    if (tagInfo == null) {
	        return false;
	    }
		return tagInfo.isConnectGroup();
	}

	/**
	 * Root 태그인 경우 자기 자신, 자식 태그인 경우 부모 태그를 반환한다.
	 * 
	 * @return
	 */
	public Tag getRevisedTag() {
		if (isRootTag()) {
			return this;
		}
		return this.parent;
	}

	public static Tag pooledTag(String name) {
		return pooledTag(name, null);
	}

	public static Tag pooledTag(String name, Tag parent) {
		return new Tag(name.toLowerCase(), parent, null);
	}

	public static Tag newTag(String name) {
		return new Tag(name.toLowerCase(), null, null);
	}

	public static Tag groupedTag(String name, String groupId) {
		return new Tag(name.toLowerCase(), null, new TagInfo(groupId, name));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tagId == null) ? 0 : tagId.hashCode());
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
		Tag other = (Tag) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tagId == null) {
			if (other.tagId != null)
				return false;
		} else if (!tagId.equals(other.tagId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tag [tagId=" + tagId + ", name=" + name + ", taggedCount=" + taggedCount + ", pooled=" + pooled
				+ ", parent=" + parent + "]";
	}
}
