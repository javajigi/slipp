package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import play.Logger;
import siena.Column;
import siena.Generator;
import siena.Id;
import siena.Index;
import siena.Model;
import siena.NotNull;
import siena.Query;

public class ThreadTag extends Model {
	@Id(Generator.AUTO_INCREMENT)
	private Long id;
	
	@NotNull
	@Column("thread_id")
	@Index("thread_tag_index")
	private Thread thread;
	
	@NotNull
	@Column("tag_id")
	@Index("tag_index")
	private Tag tag;
	
	@Index("thread_tag_created_date")
	private Date createdDate;
	
	public ThreadTag() {
	}
	
	private ThreadTag(Thread thread, Tag tag, Date createdDate) {
		this.thread = thread;
		this.tag = tag;
		this.createdDate = createdDate;
	}
	
	public Long getId() {
		return id;
	}

	public Thread getThread() {
		if (thread != null) {
			thread.get();
		}
		return thread;
	}
	
	public Tag getTag() {
		if (tag != null){
			tag.get();
		}
		return tag;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	
	static Query<ThreadTag> all() {
		return Model.all(ThreadTag.class);
	}
	
	static List<Tag> findTagsByThread(Thread thread) {
		if (thread==null || thread.getId()==null){
			return new ArrayList<Tag>();
		}
		
		List<ThreadTag> qTags = all().filter("thread", thread).fetch();
		List<Tag> tags = new ArrayList<Tag>();
		for (ThreadTag threadTag : qTags) {
			tags.add(threadTag.getTag());
		}
		
		return tags;
	}
	
	public static Integer countByTag(Tag tag) {
		return all().filter("tag", tag).count();
	}

	public static ThreadTag find(Thread thread, Tag tag) {
		return all().filter("thread", thread).filter("tag", tag).get();
	}

	static List<Thread> findThreadsByTag(Tag tag, int offset, int countPerPage) {
		List<ThreadTag> qTags = all().filter("tag", tag).order("-createdDate").fetch(countPerPage, offset);
		List<Thread> threads = new ArrayList<Thread>();
		for (ThreadTag each : qTags) {
			Thread thread = each.getThread();
			Logger.debug("Thread : %s", thread);
			threads.add(thread);
		}
		
		return threads;
	}

	static ThreadTag tagged(Thread thread, Tag tag, Date createdDate) {
		Tag originTag = tag.getParentTag();
		ThreadTag threadTag = find(thread, originTag);
		if (threadTag == null) {
			threadTag = new ThreadTag(thread, originTag, createdDate);
			threadTag.insert();
			originTag.updateTaggedCount(countByTag(originTag));
		}
		return threadTag;
	}

	static void deTagged(Thread thread, Tag tag) {
		Tag originTag = tag.getParentTag();
		ThreadTag threadTag = find(thread, originTag);
		if (threadTag != null) {
			 threadTag.delete();
			 originTag.updateTaggedCount(countByTag(originTag));
		}
	}
}
