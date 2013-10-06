package net.slipp.domain.summary;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImageFromTag extends AbstractImageFromTag{

	private TagType tagType;
	
	public ImageFromTag(TagType tagType){
		this.tagType = tagType;
	}
	
	public String image(Document doc) {
		Element element = doc.head();
		Elements imgElements = element.getElementsByTag(tagType.getTag());
		for (Element imgElement : imgElements) {
			if (hasImage(imgElement)) {
				return imgElement.attr(tagType.getAttrName());
			}
		}
		return null;
	}

	private boolean hasImage(Element imgElement) {
		return tagType.matchResource(imgElement.attr(tagType.getAttrResource()));
	}
}
