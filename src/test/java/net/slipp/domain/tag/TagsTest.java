package net.slipp.domain.tag;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static net.slipp.domain.tag.TagTest.*;
import static net.slipp.domain.tag.NewTagTest.*;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;

public class TagsTest {
	public static final Tags DEFAULT_TAGS = createTags(JAVA, NEWTAG);
	
    @Test
    public void processTags() throws Exception {
        Tags tags = createTags(JAVA, NEWTAG);
        
        tags.processTags();
        
        Set<Tag> pooledTags = tags.getPooledTags();
        assertThat(pooledTags.contains(JAVA), is(true));
        assertThat(JAVA.getTaggedCount(), is(1));
        Set<NewTag> newTags = tags.getNewTags();
        assertThat(newTags.contains(NEWTAG), is(true));
    }
    
    public static Tags createTags(Tag tag, NewTag newTag) {
    	Set<Tag> originalTags = Sets.newHashSet();
        Tags tags = new Tags(originalTags);
        tags.addTag(tag);
        tags.addNewTag(newTag);
    	return tags;
    }
}
