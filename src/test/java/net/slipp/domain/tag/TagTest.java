package net.slipp.domain.tag;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static net.slipp.domain.tag.TagBuilder.*;

import org.junit.Test;

public class TagTest {
	public static final Tag JAVA = Tag.pooledTag("java");
	public static final Tag JAVA_CHILD = Tag.pooledTag("자바", JAVA);
	public static final Tag JAVASCRIPT = Tag.pooledTag("javascript");
	public static final Tag NEWTAG = Tag.newTag("newTag");
	
	@Test
	public void equalsAndContainsTag() throws Exception {
		Tag java1 = aTag().withName("java").withPooled(true).build();
		Tag java2 = aTag().withName("java").withPooled(true).build();
		assertThat(java1.equals(java2), is(true));
	}
	
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
        Tag dut = Tag.pooledTag("java");
        dut.tagged();
        assertThat(dut.getTaggedCount(), is(1));
    }

    @Test
    public void deTagged() throws Exception {
        Tag dut = Tag.pooledTag("java");
        dut.tagged();
        dut.tagged();
        assertThat(dut.getTaggedCount(), is(2));

        dut.deTagged();
        assertThat(dut.getTaggedCount(), is(1));
    }
    
    @Test
	public void createNewTag() throws Exception {
		String name = "newTag";
		Tag dut = Tag.newTag(name);
		assertThat(dut.isPooled(), is(false));
		assertThat(dut.getName(), is(name));
		assertThat(dut.getParent(), is(nullValue()));
	}
    
    @Test
	public void movePooled() throws Exception {
    	Tag parent = null;
		Tag newTag = Tag.newTag("newTag");
		newTag.movePooled(parent);
		assertThat(newTag.isPooled(), is(true));
		
		parent = Tag.pooledTag("java");
		newTag = Tag.newTag("newTag");
		newTag.movePooled(parent);
		assertThat(newTag.isPooled(), is(true));
		assertThat(newTag.getParent(), is(parent));
	}
}
