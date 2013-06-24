package net.slipp.domain.fb;

public class FacebookGroup {
    private String groupId;
    
    private String name;
    
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
