package net.slipp.support.wiki;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikiContentsTest {
	private static final Logger logger = LoggerFactory.getLogger(WikiContentsTest.class);
	
	@Test
	public void parse_bullet() throws Exception {
		String source = "* An item in a bulleted (unordered) list \n* Another item in a bulleted list"
				+ "\n** Second Level\n** Second Level Items\n*** Third level";
		logger.debug("html : {}", WikiContents.convert(source));
	}
	
	@Test
	public void parse_code() throws Exception {
		String source = "{code:title=java}\n WikiContents wikiContents = new WikiContents(); {code}";
		logger.debug("html : {}", WikiContents.convert(source));		
	}
}