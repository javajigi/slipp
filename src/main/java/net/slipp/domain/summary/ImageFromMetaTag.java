package net.slipp.domain.summary;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImageFromMetaTag extends ImageFromTag{

	public String image(Document doc) {
		Element element = doc.head();
		Elements imgElements = element.getElementsByTag("meta");
		for (Element imgElement : imgElements) {
			if (hasImage(imgElement)) {
				return imgElement.attr("content");
			}
		}
		return null;
	}

	private boolean hasImage(Element imgElement) {
		return "og:image".equals(imgElement.attr("property"));
	}
}
