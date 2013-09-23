package net.slipp.domain.summary;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;

public class SiteImage {

	public static String getImage(Document doc) {
		ImageFromLinkTag imageFromLinkTag = new ImageFromLinkTag();
		ImageFromImgTag imageFromImgTag = new ImageFromImgTag();

		String imageUrl = imageFromLinkTag.image(doc);
		if (!StringUtils.isBlank(imageUrl)) {
			return imageUrl;
		}
		return imageFromImgTag.image(doc);
	}

}
