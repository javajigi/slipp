package net.slipp.domain.summary;

import java.io.Serializable;

import org.jsoup.nodes.Document;

public class SiteSummary implements Serializable {

	private static final long serialVersionUID = -7183093514202968034L;

	private String title;
	private String contents;
	private String thumbnailImage;
	private String targetUrl;

	public SiteSummary(String title, String contents, String thumbnailImage, String targetUrl) {
		this.title = title;
		this.contents = contents;
		this.thumbnailImage = thumbnailImage;
		this.setTargetUrl(targetUrl);
	}

	public SiteSummary(Document doc, String targetUrl) {
		this.title = doc.title();
		this.thumbnailImage = SiteImage.getImage(targetUrl, doc);
		this.contents = SiteContents.getContents(doc);
		this.setTargetUrl(targetUrl);
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

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
}
