package net.slipp.domain.summary;

import org.jsoup.nodes.Document;

public class ImageFromLogo extends AbstractImageFromTag {

	public String image(Document doc) {
		return SiteDefaultLogo.findDefaultLogo(doc.baseUri());
	}

}
