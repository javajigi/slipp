package net.slipp.support.wiki.pegdown;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.pegdown.PegDownProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PegDownProcessorTest {
	private static final Logger log = LoggerFactory.getLogger(PegDownProcessorTest.class);
	
	private PegDownProcessor dut;
	
	@Before
	public void setup() {
		dut = new PegDownProcessor();
	}
	
	@Test
	public void header() throws Exception {
		String result = dut.markdownToHtml("## 제목");
		assertThat(result, is("<h2>제목</h2>"));
		
		result = dut.markdownToHtml("#### 제목");
		assertThat(result, is("<h4>제목</h4>"));
	}
	
	@Test
	public void list() throws Exception {
		String result = dut.markdownToHtml("* 리스트1\r* 리스트2");
		log.debug("result: {}", result);
		
		result = dut.markdownToHtml("- 리스트1");
		log.debug("result: {}", result);
	}
}
