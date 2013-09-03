package net.slipp.domain.tag;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;

public class TagsTest {
    @Test
    public void getConnectedGroupTag() throws Exception {
        Tag java1 = Tag.pooledTag("java"); 
        Tag connectedTag = Tag.groupedTag("SLiPP", "1234567"); 
        Set<Tag> tagSet = Sets.newHashSet(java1, connectedTag);
        Tags tags = new Tags(tagSet);
        Set<Tag> groupTags = tags.getConnectedGroupTags();
        assertTrue(groupTags.contains(connectedTag));
    }
    
    @Test
    public void getConnectedGroupTagHasNotConntectedGroup() throws Exception {
        Tag java1 = Tag.pooledTag("java");
        Tag c = Tag.pooledTag("c");

        Set<Tag> tagSet = Sets.newHashSet(java1, c);
        Tags tags = new Tags(tagSet);
        Set<Tag> groupTags = tags.getConnectedGroupTags();
        assertTrue(groupTags.isEmpty());
    }
    
    @Test
    public void getConnectedGroupTagEmptyTag() throws Exception {
        Set<Tag> tagSet = Sets.newHashSet();
        Tags tags = new Tags(tagSet);
        Set<Tag> groupTags = tags.getConnectedGroupTags();
        assertTrue(groupTags.isEmpty());
    }    
}
