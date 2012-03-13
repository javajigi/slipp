package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import play.Logger;
import play.data.validation.Required;
import siena.Filter;
import siena.Generator;
import siena.Id;
import siena.Model;
import siena.Query;
import siena.embed.Embedded;
import supports.DateTimeUtils;
import supports.Pager;
import supports.wiki.WikiContents;

public class Thread extends Model {
	@Id(Generator.AUTO_INCREMENT)
	private Long id;
	
	@Required
	@Embedded
	private SlippUser user;
	
	@Required
	private String title;
	
	@Required
	private String contents;
	private Date createdDate;
	private Date modifiedDate;
	
	@Filter("thread")
	public Query<Answer> answers;
	
	@Filter("thread")
	public Query<ThreadTag> threadTags;

	private int answerCount;

	private int showCount;

	public Thread() {}
	
	public Thread(Long id) {
		this.id = id;
	}

	public Thread(SlippUser user, String title, String contents, Date createdDate) {
		this.user = user;
		this.title = title;
		this.contents = contents;
		this.createdDate = createdDate;
	}
	
	public Long getId() {
		return id;
	}

	public SlippUser getUser() {
		return user;
	}

	public String getTitle() {
		return title;
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
	
	public int getAnswerCount() {
		return answerCount;
	}
	
	public int getShowCount() {
		return showCount;
	}
	
	public void modify(SlippUser modifiedUser, String title, String contents, Date modifiedDate, List<Tag> tags) throws HasNotRoleException {
		checkModifyRole(modifiedUser);
		
		this.title = title;
		this.contents = contents;
		this.modifiedDate = modifiedDate;
		update();
		
		List<Tag> original = getTags();
		List<Tag> addedTags = notContains(tags, original);
		for (Tag each : addedTags) {
			Logger.debug("added tag : " + each);
			tag(each);
		}
		
		List<Tag> removedTags = notContains(original, tags);
		for (Tag each : removedTags) {
			ThreadTag.deTagged(this, each);
		}
	}
	
	static List<Tag> notContains(List<Tag> source, List<Tag> target) {
		List<Tag> tags = new ArrayList<Tag>();
		for (Tag each : source) {
			if (!target.contains(each)){
				tags.add(each);
			}			
		}
		return tags;
	}
	
	public Answer answer(SlippUser answeredUser, String contents, Date answeredDate) {
		Answer answer = new Answer(this, answeredUser, contents, answeredDate);
		answer.insert();
		this.answerCount = Answer.countByThread(this);
		update();
		return answer;
	}
	
	public List<Answer> getAnswers() {
		return answers.order("createdDate").fetch();
	}

	static Query<Thread> all(){
		return Model.all(Thread.class);
	}

	public static Thread findById(Long id) {
		return all().filter("id", id).get();
	}
	
	public static List<Thread> finds(int offset, int countPerPage) {
		return all().order("-createdDate").fetch(countPerPage, offset);
	}
	
	public static List<Thread> finds(Pager pager) {
		return all().order("-createdDate").fetch(pager.getCountPerPage(), pager.getOffset());
	}

	public void tag(Tag tag) {
		ThreadTag.tagged(this, tag, DateTimeUtils.now());
	}

	public List<Tag> getTags() {
		return ThreadTag.findTagsByThread(this);
	}
	
	public String getTagsForDisplay() {
		StringBuilder sb = new StringBuilder();
		for (Tag tag : getTags()) {
			sb.append(tag.getName());
			sb.append(" ");
		}
		return sb.toString();
	}
	
	public Boolean checkModifyRole(SlippUser loginUser) throws HasNotRoleException {
		if (loginUser==null) {
			throw new HasNotRoleException("이 쓰레드의 상태를 변경할 수 있는 권한이 없습니다.");
		}
		
		if (!user.equals(loginUser)) {
			throw new HasNotRoleException("이 쓰레드의 상태를 변경할 수 있는 권한이 없습니다.");
		}
		
		return Boolean.TRUE;
	}
	
	public boolean hasAnswer() {
		return answerCount > 0;
	}
	
	public static Thread show(Long id) {
		Thread thread = findById(id);
		Logger.debug("id : %s, showCount : %s", id, thread.getShowCount());
		thread.showCount = thread.getShowCount() + 1;
		thread.update();
		return thread;
	}
	
	public void deleteAll() {
		for (Tag each : getTags()) {
			ThreadTag.deTagged(this, each);
		}
		delete();
	}

	@Override
	public String toString() {
		return "Thread [id=" + id + ", user=" + user + ", title=" + title + ", contents=" + contents
				+ ", createdDate=" + createdDate + ", modifiedDate=" + modifiedDate + ", answerCount=" + answerCount
				+ "]";
	}
}
