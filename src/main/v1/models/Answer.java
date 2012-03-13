package models;

import java.util.Date;
import java.util.List;

import siena.Column;
import siena.Generator;
import siena.Id;
import siena.Index;
import siena.Model;
import siena.NotNull;
import siena.Query;
import siena.embed.Embedded;
import supports.DateTimeUtils;
import supports.ServiceType;
import supports.wiki.WikiContents;

public class Answer extends Model {
	@Id(Generator.AUTO_INCREMENT)
	private Long id;
	
	@NotNull
	@Column("thread_id")
	@Index("thread_index")
	private Thread thread;
	@NotNull
	@Embedded
	private SlippUser user;
	@NotNull
	private String contents;
	@NotNull
	private Date createdDate;
	private Date modifiedDate;
	
	public Answer() {}
	
	public Answer(Long id) {
		this.id = id;
	}

	public Answer(Thread thread, SlippUser user, String contents, Date createdDate) {
		this.thread = thread;
		this.user = user;
		this.contents = contents;
		this.createdDate = createdDate;
	}
	
	public Long getId() {
		return id;
	}
	
	public Thread getThread() {
		return thread;
	}

	public SlippUser getUser() {
		return user;
	}

	public String getDisplayContents() {
		return WikiContents.convert(getContents());
	}

	public String getContents() {
		return contents;
	}

	public Date getCreatedDate() {
		return createdDate;
	}
	
	public String getDisplayDate() {
		return DateTimeUtils.articleDate(getCreatedDate(), DateTimeUtils.DateFormat.LONG);
	}
	
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void modify(SlippUser modifiedUser, String contents, Date modifiedDate) throws HasNotRoleException {
		checkModifyRole(modifiedUser);

		this.contents = contents;
		this.modifiedDate = modifiedDate;
		
		update();
	}
	
	static Query<Answer> all(){
		return Model.all(Answer.class);
	}

	public static Answer findById(Long id) {
		return all().filter("id", id).get();
	}

	public static List<Answer> findByThread(Thread thread) {
		return all().filter("thread", thread).fetch();
	}
	
	public static int countByThread(Thread thread) {
		return all().filter("thread", thread).count();
	}
	
	public Boolean checkModifyRole(SlippUser loginUser) throws HasNotRoleException {
		if (loginUser==null) {
			throw new HasNotRoleException("답글을 수정할 수 있는 권한이 없습니다.");
		}
		
		if (!user.equals(loginUser)) {
			throw new HasNotRoleException("답글을 수정할 수 있는 권한이 없습니다.");
		}
		
		return Boolean.TRUE;
	}
}
