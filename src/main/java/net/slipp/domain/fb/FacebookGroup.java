package net.slipp.domain.fb;

import com.restfb.Facebook;

public class FacebookGroup {
	@Facebook("gid")
    private String groupId;
    
    @Facebook
    private String name;
    
    public FacebookGroup() {
	}
    
    public FacebookGroup(String groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }
    
    public String getGroupId() {
        return groupId;
    }
    
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "FacebookGroup [groupId=" + groupId + ", name=" + name + "]";
    }
}
