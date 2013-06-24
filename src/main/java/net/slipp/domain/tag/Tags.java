package net.slipp.domain.tag;

import java.util.Set;

public class Tags {
    private Set<Tag> tags;

    public Tags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Tag getConnectedGroupTag() {
        for (Tag tag : tags) {
            if (!tag.isPooled()) {
                continue;
            }
            
            if (tag.isConnectGroup()) {
                return tag;
            }
        }
        return null;
    }

}
