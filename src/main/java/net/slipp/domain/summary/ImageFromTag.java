package net.slipp.domain.summary;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;

public abstract class ImageFromTag {

	public abstract String image(Document doc);
	
	public String getImagePath(Document doc){
		String url = image(doc);
		if( hasNotImageUrl(url) ) {
			return null;
		}
		return getPath(url, doc);
	}

	private boolean hasNotImageUrl(String url) {
		return StringUtils.isBlank(url);
	}
	
	private String getPath(String url, Document doc) {
		if( url.indexOf("http") == 0 || hasNotImageUrl(url)){
			return url;
		}
		if( url.indexOf("//") == 0 ) {
			return "http:"+url;
		}
		return doc.baseUri().replaceAll("(?i:(https?://[^/]+)/.*)", "$1")+url;
	}
}
