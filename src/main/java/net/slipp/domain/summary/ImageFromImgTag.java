package net.slipp.domain.summary;

import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImageFromImgTag extends ImageFromTag {

	public static final List<String> excludes = Arrays.asList("feeds");

	public String image(Document doc) {
		Element element = doc.body();
		element.getElementsByTag("header").remove();
		Elements imgs = element.getElementsByTag("img");
		for (Element img : imgs) {
			if (hasSpecAttribute(img) && hasSize(img)) {
				return img.attr("src");
			}
		}
		return ifEmptyDefaultImage(imgs);
	}

	private String ifEmptyDefaultImage(Elements imgs) {
		for (Element img : imgs) {
			for (String exclude : excludes) {
				if( img.attr("src").indexOf(exclude) < 0) {
					return img.attr("src");
				}
			}
		}
		return null;
	}

	private boolean hasSize(Element img) {
		return Integer.parseInt(img.attr("width")) > 50
				&& Integer.parseInt(img.attr("height")) > 50;
	}

	private boolean hasSpecAttribute(Element img) {
		return img.hasAttr("src") && img.hasAttr("width")
				&& img.hasAttr("height");
	}

}
