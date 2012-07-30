package net.slipp.domain.tag;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class NewTag {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long tagId;

	@Column(name = "name", length = 50, nullable = false)
	private String name;
	
	private int taggedCount = 1;
	
	public NewTag() {
	}
	
	public NewTag(String name) {
		this.name = name;
	}
	
	public Long getTagId() {
		return tagId;
	}
	
	public String getName() {
		return name;
	}
	
	public void tagged() {
		taggedCount += 1;
	}

	public int getTaggedCount() {
		return taggedCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tagId == null) ? 0 : tagId.hashCode());
		result = prime * result + taggedCount;
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
		NewTag other = (NewTag) obj;
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
		if (taggedCount != other.taggedCount)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NewTag [tagId=" + tagId + ", name=" + name + ", taggedCount=" + taggedCount + "]";
	}
}
