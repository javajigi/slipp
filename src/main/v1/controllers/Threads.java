package controllers;

import java.util.Date;
import java.util.List;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import models.Answer;
import models.Auth;
import models.HasNotRoleException;
import models.Tag;
import models.Thread;
import play.Logger;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;
import supports.DateTimeUtils;
import supports.Pager;
import supports.web.Check;
import supports.web.GAESecure;
import supports.web.Role;
import supports.wiki.WikiContents;

@With(GAESecure.class)
public class Threads extends Controller {
	private static final int DEFAULT_PAGE_NO = 1;
	private static int DEFAULT_RSS_COUNT = 10;
	
	public static void list(int pageNo) {
		Pager pager = new Pager(pageNo);
		List<Thread> threads = Thread.finds(pager);
		int previousPageNo = pager.getPreviousPageNo();
		int nextPageNo = pager.getNextPageNo(threads.size());
		List<Tag> sortedTags = Tag.findByTaggedCount();
		render(threads, sortedTags, previousPageNo, nextPageNo);
	}
	
	@Check(Role.ROLE_USER)
	public static void createForm(String tagName) {
		renderArgs.put("thread", new Thread());
		renderArgs.put("tagName", tagName);
		renderArgs.put("sortedTags", Tag.findByTaggedCount());
		render("Threads/form.html");
	}
	
	@Check(Role.ROLE_USER)
	public static void updateForm(Long id) {
		Thread thread = new Thread(id);
		thread.get();
		
		try {
			thread.checkModifyRole(Auth.getSlippUser());
			renderArgs.put("thread", thread);
			renderArgs.put("sortedTags", Tag.findByTaggedCount());
			render("Threads/form.html");
		} catch (HasNotRoleException e) {
			show(id, e.getMessage());
		}
	}
	
	@Check(Role.ROLE_USER)
	public static void save(Long id, @Required String title, @Required String contents, String tagnames) {
		if (validation.hasErrors()) {
			Thread thread = id == null ? new Thread() : getThread(id);
			render("Threads/form.html", thread);
		}

		if (id == null) {
			Thread thread = new Thread(Auth.getSlippUser(), title, contents, DateTimeUtils.now());
			List<Tag> tags = null;
			try {
				tags = TagHelper.getTagsFromName(TagHelper.parse(tagnames));
			} catch (TagNotFoundException e) {
				renderArgs.put("errorMessage", e.getMessage());
				render("Threads/form.html", thread);
			}
			
			thread.insert();
			
			for (Tag tag : tags) {
				thread.tag(tag);
			}
			
			Tag.ms.put(Tag.SORTED_TAGS_CACHE_KEY, Tag.findSortedTags());
			show(thread.getId(), null);
		} else {
			Thread thread = getThread(id);
			List<Tag> tags = null;
			try {
				tags = TagHelper.getTagsFromName(TagHelper.parse(tagnames));
			} catch (TagNotFoundException e) {
				renderArgs.put("errorMessage", e.getMessage());
				render("Threads/form.html", thread);
			}
			
			try {
				thread.modify(Auth.getSlippUser(), title, contents, DateTimeUtils.now(), tags);
			} catch (HasNotRoleException e) {
				renderArgs.put("errorMessage", e.getMessage());
				render("Threads/form.html", thread);
			}
			Tag.ms.put(Tag.SORTED_TAGS_CACHE_KEY, Tag.findSortedTags());
			show(id, null);
		}
	}
	
	public static void show(Long id, String errorMessage) {
		Thread thread = Thread.show(id);
		List<Answer> answers = thread.getAnswers();
		List<Tag> sortedTags = Tag.findByTaggedCount();
		render(thread, answers, sortedTags, errorMessage);
	}
	
	@Check(Role.ROLE_USER)
	public static void answer(Long threadId, @Required String contents) {
		Thread thread = Thread.findById(threadId);
		thread.answer(Auth.getSlippUser(), contents, DateTimeUtils.now());
		show(threadId, null);
	}
	
	public static void tagged(String tagName, int pageNo) {
		Logger.debug("tagName : %s, pageNo : %s", tagName, pageNo);
		Pager pager = new Pager(pageNo);
		Tag tag = Tag.findByName(tagName);
		List<Thread> threads = tag.getThreads(pager);
		int previousPageNo = pager.getPreviousPageNo();
		int nextPageNo = pager.getNextPageNo(threads.size());
		List<Tag> sortedTags = Tag.findByTaggedCount();
		render("Threads/list.html", threads, sortedTags, tagName, previousPageNo, nextPageNo);
	}
	
	@Check(Role.ROLE_USER)
	public static void delete(Long id) {
		Thread thread = getThread(id);
		try {
			thread.checkModifyRole(Auth.getSlippUser());
		} catch (HasNotRoleException e) {
			show(id, e.getMessage());
		}
		if (thread.hasAnswer()){
			show(id, "답변 글을 가지는 쓰레드는 삭제할 수 없습니다.");
		}
		thread.deleteAll();
		list(1);
	}
	
	public static void rss(String tagName){
		Tag tag = Tag.findByName(tagName);
		if (tag==null){
			list(DEFAULT_PAGE_NO);
		} else {
			List<Thread> threads = tag.getThreads(new Pager(DEFAULT_PAGE_NO, DEFAULT_RSS_COUNT));
			Date now = DateTimeUtils.now();
			render(threads, now);
		}
	}
	
	@Check(Role.ROLE_USER)
	public static void updateAnswerForm(Long id, Long answerId){
		Thread thread = getThread(id);
		Answer answer = Answer.findById(answerId);
		try {
			answer.checkModifyRole(Auth.getSlippUser());
		} catch (HasNotRoleException e) {
			show(id, e.getMessage());
		}
		List<Tag> sortedTags = Tag.findByTaggedCount();
		render("Threads/answerForm.html", thread, answer, sortedTags);
	}
	
	@Check(Role.ROLE_USER)
	public static void updateAnswer(Long id, Long answerId, @Required String contents){
		Answer answer = Answer.findById(answerId);
		try {
			answer.modify(Auth.getSlippUser(), contents, DateTimeUtils.now());
		} catch (HasNotRoleException e) {
			show(id, e.getMessage());
		}
		show(id, null);
	}
	
	public static void preview(String data) {
		Logger.debug("preview data : %s", data);
		String previewData = WikiContents.convert(data);
		render(previewData);
	}
	
	private static Thread getThread(Long id) {
		Thread thread = new Thread(id);
		thread.get();
		return thread;
	}
}
