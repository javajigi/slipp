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
    
    public boolean isEmpty() {
        return false;
    }
    
    public static class EmptyFacebookGroup extends FacebookGroup {
        public EmptyFacebookGroup() {
            super(null, null);
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FacebookGroup other = (FacebookGroup) obj;
        if (groupId == null) {
            if (other.groupId != null)
                return false;
        } else if (!groupId.equals(other.groupId))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "FacebookGroup [groupId=" + groupId + ", name=" + name + "]";
    }
}
