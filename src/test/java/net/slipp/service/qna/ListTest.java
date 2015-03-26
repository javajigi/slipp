package net.slipp.service.qna;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class ListTest {
	private static final Logger logger = LoggerFactory.getLogger(ListTest.class);
	
	@Test
	public void subList() throws Exception {
		List<String> values = Lists.newArrayList("a", "b", "c");
		List<String> subValues = values.subList(0, 2);
		logger.debug("subValues : {}", subValues);
		
		int length = 5;
		if (values.size() < length) {
			length = values.size();
		}
		subValues = values.subList(0, length);
		logger.debug("subValues : {}", subValues);
	}
}
