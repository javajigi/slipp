package net.slipp.support.wiki;

import java.io.StringWriter;

import org.eclipse.mylyn.wikitext.confluence.core.ConfluenceLanguage;
import org.eclipse.mylyn.wikitext.core.parser.DocumentBuilder;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.EventLoggingDocumentBuilder;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.junit.Test;

public class WikiTextParserTest {
	@Test
	public void event_logging() {
		String source = "* An item in a bulleted (unordered) list" + "* Another item in a bulleted list"
				+ "** Second Level" + "** Second Level Items" + "*** Third level";
		StringWriter writer = new StringWriter();
		DocumentBuilder builder = new EventLoggingDocumentBuilder();

		MarkupParser parser = new MarkupParser(new ConfluenceLanguage());
		parser.setBuilder(builder);
		parser.parse(source);

		System.out.println(writer.toString());
	}

	@Test
	public void parse1() throws Exception {
		String source = "*테스트*";
		StringWriter writer = new StringWriter();
		HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer);
		builder.setEmitAsDocument(false);
		MarkupParser parser = new MarkupParser(new ConfluenceLanguage());
		parser.setBuilder(builder);
		parser.parse(source);

		System.out.println(writer.toString());
	}

	@Test
	public void parse2() throws Exception {
		String source = "h1. 테스트";
		StringWriter writer = new StringWriter();
		HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer);
		builder.setEmitAsDocument(false);
		MarkupParser parser = new MarkupParser(new ConfluenceLanguage());
		parser.setBuilder(builder);
		parser.parse(source);

		System.out.println(writer.toString());
	}
}
