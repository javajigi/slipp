package net.slipp.domain.smalltalk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import net.slipp.domain.user.SocialUser;
import net.slipp.support.jpa.CreatedAndUpdatedDateEntityListener;
import net.slipp.support.jpa.HasCreatedAndUpdatedDate;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

@Entity
@EntityListeners({ CreatedAndUpdatedDateEntityListener.class })
public class SmallTalkComment implements HasCreatedAndUpdatedDate {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long smallTalkCommentId;

	@NotNull
	@Length(min = 1, max = 200)
	@Column(name = "comments", length = 255, nullable = false)
	private String comments;

	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name = "fk_smalltalkcomment_writer"))
	private SocialUser writer;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false, updatable = false)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date", nullable = false)
	private Date updatedDate;
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name = "fk_smalltalkcomment_parent_id"))
	private SmallTalk smallTalk;
	
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public SmallTalk getSmallTalk() {
		return smallTalk;
	}

	public void setSmallTalk(SmallTalk smallTalk) {
		this.smallTalk = smallTalk;
	}

	public void commentTo(SmallTalk smallTalk){
		setSmallTalk(smallTalk);
		
	}

	public SocialUser getWriter() {
		return writer;
	}

	public void setWriter(SocialUser writer) {
		this.writer = writer;
	}

	public String getComments() {
		return StringUtils.trim(comments);
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getSmallTalkCommentId() {
		return smallTalkCommentId;
	}

	public void setSmallTalkCommentId(Long smallTalkCommentId) {
		this.smallTalkCommentId = smallTalkCommentId;
	}
}
