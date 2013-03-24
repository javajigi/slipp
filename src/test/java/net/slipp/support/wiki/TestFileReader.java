package net.slipp.support.wiki;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestFileReader {
	private static final Logger logger = LoggerFactory.getLogger(TestFileReader.class);

	public static String read(Object target, String fileName) {
		try {
			String location = target.getClass().getPackage().getName().replaceAll("\\.", "/") + "/" + fileName;
			logger.debug("location : {}", location);
			return IOUtils.toString(target.getClass().getClassLoader().getResourceAsStream(location), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException("Cannot load ", e);
		}
	}
}
