package net.slipp.domain.qna;

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

	@Column(name = "name", length = 50, nullable = false)
	private String name;

	private int taggedCount = 0;

	@OneToOne
	@org.hibernate.annotations.ForeignKey(name = "fk_tag_parent_id")
	public Tag parent;
	
	public Tag() {
	}

	public Tag(String name) {
		this.name = name;
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
}
