package net.slipp.domain.summary;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;

public class SiteImage {

	public static String getImage(Document doc) {
		ImageFromTag imageFromMetaTag = new ImageFromMetaTag();
		ImageFromTag imageFromLinkTag = new ImageFromLinkTag();
		ImageFromTag imageFromImgTag = new ImageFromImgTag();

		String imageUrl = imageFromMetaTag.getImagePath(doc);
		if (!StringUtils.isBlank(imageUrl)) {
			return imageUrl;
		}
		imageUrl = imageFromImgTag.getImagePath(doc);
		if (!StringUtils.isBlank(imageUrl)) {
			return imageUrl;
		}
		return imageFromLinkTag.getImagePath(doc);
	}

}
