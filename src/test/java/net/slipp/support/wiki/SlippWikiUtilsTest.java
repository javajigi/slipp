package net.slipp.support.wiki;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlippWikiUtilsTest {
	private Logger logger = LoggerFactory.getLogger(SlippWikiUtilsTest.class);

	@Test
	public void createImageList() throws Exception {
		String contents = TestFileReader.read(this, "images.txt");
		List<String> images = SlippWikiUtils.createImageListFrom(contents);
		assertThat(images.size(), is(2));
		logger.debug(images.toString());
	}
}
