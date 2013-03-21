package net.slipp.domain.wiki;

import java.io.Serializable;
import java.sql.Timestamp;

public class WikiPage implements Serializable {
	private static final long serialVersionUID = 2244328207756692193L;

	private static final int DEFAULT_SHORT_CONTENTS_LENGTH = 150;
	
	private Long pageId;
	private String title;
	private Timestamp creationDate;
	private String contents;

	public WikiPage(Long pageId, String title, Timestamp creationDate, String contents) {
		this.pageId = pageId;
		this.title = title;
		this.creationDate = creationDate;
		this.contents = contents;
	}
	
	public Long getPageId() {
		return pageId;
	}

	public String getTitle() {
		return title;
	}
	
	public Timestamp getCreationDate() {
		return creationDate;
	}
	
	public String getContents() {
		return contents;
	}
	
	public String getShortContents() {
		String stripedContents = contents.replaceAll("\\<[^>]*>","");
		if (stripedContents.length() < DEFAULT_SHORT_CONTENTS_LENGTH) {
			return stripedContents;
		}
		
		return stripedContents.substring(0, DEFAULT_SHORT_CONTENTS_LENGTH) + "...";
	}
}
