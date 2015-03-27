package net.slipp.domain.summary;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;

public class SiteImage {

	public static String getImage(String targetUrl, Document doc) {
		
		AbstractImageFromTag imageFromLogo = new ImageFromLogo();
		
		AbstractImageFromTag imageFromMetaTag = new ImageFromTag(TagType.meta);
		AbstractImageFromTag imageFromLinkTag = new ImageFromTag(TagType.link);
		
		AbstractImageFromTag imageFromImgTag = new ImageFromImgTag();

		String imageUrl = imageFromLogo.getImagePath(targetUrl, doc);
		if (!StringUtils.isBlank(imageUrl)) {
			return imageUrl;
		}
		imageUrl = imageFromMetaTag.getImagePath(targetUrl, doc);
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
