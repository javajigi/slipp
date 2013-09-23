package net.slipp.domain.tag;

import java.util.Set;

import com.google.common.collect.Sets;

public class Tags {
    private Set<Tag> tags;

    public Tags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Tag> getConnectedGroupTags() {
        Set<Tag> groupTags = Sets.newHashSet();
        for (Tag tag : tags) {
            if (!tag.isPooled()) {
                continue;
            }
            
            if (tag.isConnectGroup()) {
                groupTags.add(tag);
            }
        }
        return groupTags;
    }
}
