package net.slipp.support.wiki;

import java.io.StringWriter;

import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.core.parser.markup.MarkupLanguage;

public class WikiContents {
	public static String parse(String contents) {
		return parse(contents, new SlippLanguage());
	}
	
	public static String parse(String contents, MarkupLanguage markupLanguage) {
        StringWriter writer = new StringWriter();
        HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer);
        builder.setEmitAsDocument(false);
        MarkupParser parser = new MarkupParser(markupLanguage);
        parser.setBuilder(builder);
        parser.parse(contents);
        return writer.toString();	    
	}
}
