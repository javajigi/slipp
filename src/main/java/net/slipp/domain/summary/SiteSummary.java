package net.slipp.domain.summary;

import java.io.Serializable;

import org.jsoup.nodes.Document;

public class SiteSummary implements Serializable {

	private static final long serialVersionUID = -7183093514202968034L;

	private String title;
	private String contents;
	private String thumbnailImage;

	public SiteSummary(String title, String contents, String thumbnailImage) {
		this.title = title;
		this.contents = contents;
		this.thumbnailImage = thumbnailImage;
	}

	public SiteSummary(Document doc) {
		this.title = doc.title();
		this.thumbnailImage = SiteImage.getImage(doc);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getThumbnailImage() {
		return thumbnailImage;
	}

	public void setThumbnailImage(String thumbnailImage) {
		this.thumbnailImage = thumbnailImage;
	}
}
