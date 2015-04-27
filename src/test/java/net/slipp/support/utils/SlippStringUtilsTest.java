package net.slipp.support.utils;

import org.junit.Test;
import org.pegdown.PegDownProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlippStringUtilsTest {
	private static final Logger logger = LoggerFactory.getLogger(SlippStringUtilsTest.class);

	@Test
	public void convertMarkdownLinks() {
		String contents = "Please go to http://stackoverflow.com and then mailto:oscarreyes@wordpress.com to download a file from    ftp://user:pass@someserver/someFile.txt";
		String result = SlippStringUtils.convertMarkdownLinks(contents);
		logger.debug("result : {}", result);
		
		result = new PegDownProcessor().markdownToHtml(result);
		logger.debug("result : {}", result);
	}

}
