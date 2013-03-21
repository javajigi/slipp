package net.slipp.support;

import java.io.File;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleTest {
	private static Logger log = LoggerFactory.getLogger(SimpleTest.class);
	
	@Test
	public void test() throws Exception {
		Process process = Runtime.getRuntime().exec("ls");
		
		StringWriter writer = new StringWriter();
		IOUtils.copy(process.getInputStream(), writer, "UTF-8");
		log.debug(writer.toString());
	}
	
	@Test
	public void currentDir() throws Exception {
		File file = new File("webapp\\WEB-INF\\static_resources");
		log.debug("static path : {}", file.getAbsolutePath());
	}
}
