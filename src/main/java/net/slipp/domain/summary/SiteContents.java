package net.slipp.domain.summary;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SiteContents {

	public static String getContents(Document doc) {
		Element element = doc.body();
		element.getElementsByTag("header").remove();
		Elements elements = element.getElementsByTag("p");
		for (Element contentElement : elements) {
			String text = contentElement.text();
			if( text.length() > 10 ){
				return text;
			}
		}
		return null;
	}

}
