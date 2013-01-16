package net.slipp.domain.tag;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {
	public static final Tag JAVA = new Tag("java");
	public static final Tag JAVA_CHILD = new Tag("자바", JAVA);
	public static final Tag JAVASCRIPT = new Tag("javascript");
	
    @Test
    public void addChild() throws Exception {
        Tag actual = JAVA_CHILD.getParent();
        assertThat(actual, is(JAVA));
    }

    @Test
    public void getRevisedTag_parentTag() throws Exception {
        assertThat(JAVA.getRevisedTag(), is(JAVA));
    }

    @Test
    public void getRevisedTag_childTag() throws Exception {
        assertThat(JAVA_CHILD.getRevisedTag(), is(JAVA));
    }

    @Test
    public void tagged() {
        Tag dut = new Tag("java");
        dut.tagged();
        assertThat(dut.getTaggedCount(), is(1));
    }

    @Test
    public void deTagged() throws Exception {
        Tag dut = new Tag("java");
        dut.tagged();
        dut.tagged();
        assertThat(dut.getTaggedCount(), is(2));

        dut.deTagged();
        assertThat(dut.getTaggedCount(), is(1));
    }
}
