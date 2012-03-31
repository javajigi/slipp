package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import net.slipp.domain.qna.Tag;

import org.junit.Before;
import org.junit.Test;

public class TagTest {
	private Tag dut;
	
	@Before
	public void setup() {
		dut = new Tag("java");
	}

	@Test
	public void tagged() {
		dut.tagged();
		assertThat(dut.getTaggedCount(), is(1));
	}
	
	@Test
	public void deTagged() throws Exception {
		dut.tagged();
		dut.tagged();
		assertThat(dut.getTaggedCount(), is(2));
		
		dut.deTagged();
		assertThat(dut.getTaggedCount(), is(1));
	}
}
