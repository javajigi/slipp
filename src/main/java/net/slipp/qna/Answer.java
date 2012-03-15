package net.slipp.qna;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import net.slipp.social.connect.SocialUser;

@Entity
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long answerId;
	
	@OneToOne
	@org.hibernate.annotations.ForeignKey(name = "fk_answer_social_user_id")
	private SocialUser user;
	
	private String contents;
	
	private Date createdDate;
	
	private Date modifiedDate;
	
	@ManyToOne
	@org.hibernate.annotations.ForeignKey(name = "fk_answer_parent_id")
	private Question question;

	void setQuestion(Question question) {
		this.question = question;
	}
	
	public Question getQuestion() {
		return this.question;
	}
}
