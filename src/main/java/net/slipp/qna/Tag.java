package net.slipp.qna;

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
	
	private String name;
	
	private int taggedCount = 0;

	@OneToOne
	@org.hibernate.annotations.ForeignKey(name = "fk_tag_parent_id")
	public Tag parent;

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
