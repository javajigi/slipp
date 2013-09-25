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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import net.slipp.domain.summary.SiteSummary;
import net.slipp.domain.user.SocialUser;
import net.slipp.support.jpa.CreatedAndUpdatedDateEntityListener;
import net.slipp.support.jpa.HasCreatedAndUpdatedDate;
import net.slipp.support.utils.SlippStringUtils;
import net.slipp.support.utils.TimeUtils;

import org.hibernate.validator.constraints.Length;

@Entity
@EntityListeners({ CreatedAndUpdatedDateEntityListener.class })
public class SmallTalk implements HasCreatedAndUpdatedDate {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long smallTalkId;

	@NotNull
	@Length(min = 1, max = 100)
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

	@Transient
	private SiteSummary siteSummary;

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

	public String getTime() {
		if (TimeUtils.diffDay(new Date(), this.createdDate) == 0) {
			return TimeUtils.agoTime(this.createdDate);
		}
		return "아주 오래전...";
	}

	public SiteSummary getSiteSummary() {
		return siteSummary;
	}

	public void setSiteSummary(SiteSummary siteSummary) {
		this.siteSummary = siteSummary;
	}

	public String getUrlInTalk() {
		return SlippStringUtils.getUrlInText(getTalk());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SmallTalk [smallTalkId=");
		builder.append(smallTalkId);
		builder.append(", talk=");
		builder.append(talk);
		builder.append(", writer=");
		builder.append(writer);
		builder.append(", createdDate=");
		builder.append(createdDate);
		builder.append(", updatedDate=");
		builder.append(updatedDate);
		builder.append(", siteSummary=");
		builder.append(siteSummary);
		builder.append("]");
		return builder.toString();
	}
}
