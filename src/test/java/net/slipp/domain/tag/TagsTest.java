package net.slipp.domain.tag;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;

public class TagsTest {
    @Test
    public void processTags() throws Exception {
        Set<Tag> originalTags = Sets.newHashSet();
        Tags tags = new Tags(originalTags);
        
        Tag java = new Tag("java");
        tags.addTag(java);
        NewTag javascript = new NewTag("javascript");
        tags.addNewTag(javascript);
        
        tags.processTags();
        
        Set<Tag> pooledTags = tags.getPooledTags();
        assertThat(pooledTags.contains(java), is(true));
        assertThat(java.getTaggedCount(), is(1));
        Set<NewTag> newTags = tags.getNewTags();
        assertThat(newTags.contains(javascript), is(true));
    }
}
