package net.slipp.domain.smalltalk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import net.slipp.domain.user.SocialUser;
import net.slipp.support.jpa.CreatedAndUpdatedDateEntityListener;
import net.slipp.support.jpa.HasCreatedAndUpdatedDate;
import net.slipp.support.utils.TimeUtils;

import org.hibernate.validator.constraints.Length;

@Entity
@EntityListeners({ CreatedAndUpdatedDateEntityListener.class })
public class SmallTalk implements HasCreatedAndUpdatedDate {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long smallTalkId;
	
	@NotNull
	@Length(min=1, max=255)
	@Column(name = "talk", length = 255, nullable = false)
	private String talk;
	
    @ManyToOne
    @org.hibernate.annotations.ForeignKey(name = "fk_smalltalk_writer")
    private SocialUser writer;
    
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false, updatable = false)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date", nullable = false)
	private Date updatedDate;
	
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

	public String getTalk() {
		return talk;
	}

	public void setTalk(String talk) {
		this.talk = talk;
	}

	public SocialUser getWriter() {
		return writer;
	}

	public void setWriter(SocialUser writer) {
		this.writer = writer;
	}
	
	public String getTime(){
		if( TimeUtils.diffDay(new Date(), this.createdDate) == 0 ) {
			return TimeUtils.agoTime(this.createdDate);
		}
		return "아주 오래전...";
	}
}
