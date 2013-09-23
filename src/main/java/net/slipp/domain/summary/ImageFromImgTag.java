package net.slipp.domain.summary;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImageFromImgTag {

	public String image(Document doc) {
		Element element = doc.body();
		Elements imgs = element.getElementsByTag("img");
		for (Element img : imgs) {
			if (hasSpecAttribute(img) && hasSize(img)) {
				return img.attr("src");
			}
		}
		return ifEmptyDefaultImage(imgs);
	}

	private String ifEmptyDefaultImage(Elements imgs) {
		return imgs.size() > 0 ? imgs.get(0).attr("src") : "";
	}

	private boolean hasSize(Element img) {
		return Integer.parseInt(img.attr("width")) > 50 && Integer.parseInt(img.attr("height")) > 50;
	}

	private boolean hasSpecAttribute(Element img) {
		return img.hasAttr("src") && img.hasAttr("width") && img.hasAttr("height");
	}

}
