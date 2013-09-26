package net.slipp.domain.summary;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;

public abstract class ImageFromTag {

	public abstract String image(Document doc);

	public String getImagePath(String targetUrl, Document doc) {
		String imageSrcUrl = image(doc);
		if (hasNotImageUrl(imageSrcUrl)) {
			return null;
		}
		return getPath(targetUrl, imageSrcUrl, doc);
	}

	private boolean hasNotImageUrl(String imageSrcUrl) {
		return StringUtils.isBlank(imageSrcUrl);
	}

	private String getPath(String targetUrl, String imageSrcUrl, Document doc) {
		if (imageSrcUrl.indexOf("http") == 0 || hasNotImageUrl(imageSrcUrl)) {
			return imageSrcUrl;
		}
		if (imageSrcUrl.indexOf("//") == 0) {
			return "http:" + imageSrcUrl;
		}
		//return doc.baseUri().replaceAll("(?i:(https?://[^/]+)/.*)", "$1") + imageSrcUrl;
		return targetUrl + FilenameUtils.getName(imageSrcUrl);
	}
}
