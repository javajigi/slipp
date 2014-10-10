package net.slipp.domain.summary;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;

public abstract class AbstractImageFromTag {

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
		if ( StringUtils.startsWith(imageSrcUrl, "http") || hasNotImageUrl(imageSrcUrl)) {
			return imageSrcUrl;
		}
		if ( StringUtils.startsWith(imageSrcUrl, "//") ) {
			return "http:" + imageSrcUrl;
		}
		if (StringUtils.startsWith(imageSrcUrl, ".") ){
			return targetUrl + FilenameUtils.getName(imageSrcUrl);
		}
		return targetUrl+imageSrcUrl;
	}
}
