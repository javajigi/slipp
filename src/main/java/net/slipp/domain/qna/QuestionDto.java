package net.slipp.domain.qna;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.slipp.domain.fb.FacebookGroup;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

public class QuestionDto {
    private static final String FACEBOOK_GROUP_DELIMETER = "::";
    
    private Long questionId;
    
    private String title;

    private String contents;
    
    private String plainTags;
    
    private boolean connected = false;
    
    private String[] plainFacebookGroups;
    
    public QuestionDto() {
    }
    
    public QuestionDto(String title, String contents, String plainTags) {
        this(null, title, contents, plainTags);
    }
    
    public QuestionDto(Long questionId, String title, String contents, String plainTags) {
        this.questionId = questionId;
        this.title = title;
        this.contents = contents;
        this.plainTags = plainTags;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getPlainTags() {
        return plainTags;
    }

    public void setPlainTags(String plainTags) {
        this.plainTags = plainTags;
    }
    
    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    
    public boolean isConnected() {
        return this.connected;
    }
    
    public String[] getPlainFacebookGroups() {
        return plainFacebookGroups;
    }

    public void setPlainFacebookGroups(String[] plainFacebookGroups) {
        this.plainFacebookGroups = plainFacebookGroups;
    }
    
    public Set<FacebookGroup> getFacebookGroups() {
        return createFacebookGroups(this.plainFacebookGroups);
    }

    static Set<FacebookGroup> createFacebookGroups(String[] fbGroups) {
        if (fbGroups == null || fbGroups.length == 0) {
            return new HashSet<FacebookGroup>();
        }
        
        Set<FacebookGroup> groups = Sets.newHashSet();
        for (String each : fbGroups) {
            groups.add(createFacebookGroup(each));
        }
        return groups;
    }
    
    static FacebookGroup createFacebookGroup(String fbGroup) {
        if (StringUtils.isBlank(fbGroup)) {
            return new FacebookGroup.EmptyFacebookGroup();
        }
        String[] parsedGroups = fbGroup.split(FACEBOOK_GROUP_DELIMETER);
        if (parsedGroups.length != 2) {
            return new FacebookGroup.EmptyFacebookGroup();
        }
        return new FacebookGroup(parsedGroups[0], replaceSpaceToDash(parsedGroups[1]));
    }
    
    static String replaceSpaceToDash(String groupName) {
        if (StringUtils.isBlank(groupName)) {
            return "";
        }
        
        return groupName.replaceAll(" ", "-");
    }

    @Override
    public String toString() {
        return "QuestionDto [questionId=" + questionId + ", title=" + title + ", contents=" + contents + ", plainTags="
                + plainTags + ", connected=" + connected + ", plainFacebookGroups="
                + Arrays.toString(plainFacebookGroups) + "]";
    }
}
