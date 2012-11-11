package net.slipp.support.web;

import java.io.File;

import org.junit.Test;

public class SimpleTest {
	@Test
	public void testName() throws Exception {
		File file = new File("./webapp/WEB-INF/");
		ProcessBuilder pb = new ProcessBuilder(file.getAbsolutePath() + "/styl.sh");
		pb.directory(new File(file.getAbsolutePath()));
		pb.start();
	}
}
