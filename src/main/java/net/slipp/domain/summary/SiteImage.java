package net.slipp.domain.summary;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;

public class SiteImage {

	public static String getImage(String targetUrl, Document doc) {
		ImageFromTag imageFromMetaTag = new ImageFromMetaTag();
		ImageFromTag imageFromLinkTag = new ImageFromLinkTag();
		ImageFromTag imageFromImgTag = new ImageFromImgTag();
		
		String imageUrl = imageFromMetaTag.getImagePath(targetUrl, doc);
		if (!StringUtils.isBlank(imageUrl)) {
			return imageUrl;
		}
		imageUrl = imageFromImgTag.getImagePath(targetUrl, doc);
		if (!StringUtils.isBlank(imageUrl)) {
			return imageUrl;
		}
		return imageFromLinkTag.getImagePath(targetUrl, doc);
	}

}
