package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import net.slipp.domain.fb.FacebookGroup;

import org.junit.Test;

public class QuestionDtoTest {
    @Test
    public void createFacebookGroups() throws Exception {
        String[] fbGroups = {"12345::생활코딩", "67890::SLiPP 스터디 1기"}; 
        Set<FacebookGroup> groups = QuestionDto.createFacebookGroups(fbGroups);
        assertTrue(groups.contains(new FacebookGroup("12345", "생활코딩")));
        assertTrue(groups.contains(new FacebookGroup("67890", "SLiPP-스터디-1기")));
    }
    
    @Test
    public void createFacebookGroup() {
        FacebookGroup group = QuestionDto.createFacebookGroup("12345::생활코딩");
        assertThat(group, is(new FacebookGroup("12345", "생활코딩")));
    }
    
    @Test
    public void replaceSpaceToUnderbar() throws Exception {
        String actual = QuestionDto.replaceSpaceToDash("SLiPP 스터디 1기");
        assertThat(actual, is("SLiPP-스터디-1기"));
    }

}
