package net.slipp.support.wiki;

import java.io.StringWriter;

import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;

public class WikiContents {
	public static String convert(String contents) {
		StringWriter writer = new StringWriter();
		HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer);
		builder.setEmitAsDocument(false);
		MarkupParser parser = new MarkupParser(new SlippLanguage());
		parser.setBuilder(builder);
		parser.parse(contents);
		return writer.toString();
	}
}
