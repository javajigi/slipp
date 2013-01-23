package net.slipp.domain.tag;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class Tags {
    private static final Logger logger = LoggerFactory.getLogger(Tags.class);
    
    private Set<Tag> pooledTags = Sets.newHashSet();
    private Set<NewTag> newTags = Sets.newHashSet();

    private Set<Tag> originalTags;
    
    public Tags(Set<Tag> originalTags) {
        this.originalTags = originalTags;
    }

    public void addTag(Tag pooledTag) {
        pooledTags.add(pooledTag);
    }
    
    public Set<Tag> getPooledTags() {
        return pooledTags;
    }

    public void addNewTag(NewTag newTag) {
        newTags.add(newTag);
    }
    
    public Set<NewTag> getNewTags() {
        return newTags;
    }
    
    public void processTags() {
        processAddTags();
        processRemoveTags();
    }

    private void processRemoveTags() {
        SetView<Tag> removedTags = Sets.difference(originalTags, pooledTags);
        logger.debug("removedTags size : {}", removedTags.size());
        for (Tag tag : removedTags) {
            tag.deTagged();
        }        
    }

    private void processAddTags() {
        SetView<Tag> addedTags = Sets.difference(pooledTags, originalTags);
        logger.debug("addedTags size : {}", addedTags.size());
        for (Tag tag : addedTags) {
            tag.tagged();
        }
    }
}
