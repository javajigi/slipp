package net.slipp.domain.tag;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long tagId;

	@Column(name = "name", length = 50, unique=true, nullable = false)
	private String name;

	private int taggedCount = 0;

	@OneToOne
	@org.hibernate.annotations.ForeignKey(name = "fk_tag_parent_id")
	public Tag parent;
	
	public Tag() {
	}
	
	public Tag(Long tagId, String name) {
		this.tagId = tagId;
		this.name = name;
	}

	public Tag(String name) {
		this(name, null);
	}

	public Tag(String name, Tag parent) {
		this.name = name;
		this.parent = parent;
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

	public void tagged() {
		taggedCount += 1;
	}

	public int getTaggedCount() {
		return taggedCount;
	}

	public void deTagged() {
		taggedCount -= 1;
	}

	public Tag getParent() {
		return this.parent;
	}
	
	private boolean isRootTag() {
	    return parent == null;
	}
	
    /**
     * Root 태그인 경우 자기 자신, 자식 태그인 경우 부모 태그를 반환한다.
     * @return
     */
    public Tag getRevisedTag() {
        if (isRootTag()) {
            return this;
        }
        return this.parent;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
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
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
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
		return "Tag [tagId=" + tagId + ", name=" + name + ", taggedCount=" + taggedCount + ", parent=" + parent + "]";
	}
}
