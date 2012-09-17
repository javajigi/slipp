package net.slipp.domain.tag;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {
    @Test
    public void addChild() throws Exception {
        Tag parent = new Tag("java");
        Tag child = new Tag("자바", parent);
        Tag actual = child.getParent();
        assertThat(actual, is(parent));
    }

    @Test
    public void getRevisedTag_parentTag() throws Exception {
        Tag parent = new Tag("java");
        assertThat(parent.getRevisedTag(), is(parent));
    }

    @Test
    public void getRevisedTag_childTag() throws Exception {
        Tag parent = new Tag("java");
        Tag child = new Tag("자바", parent);
        assertThat(child.getRevisedTag(), is(parent));
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
