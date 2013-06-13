package net.slipp.domain.tag;

import static net.slipp.domain.tag.TagBuilder.aTag;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Set;

import net.slipp.domain.ProviderType;
import net.slipp.domain.user.SocialUser;
import net.slipp.domain.user.SocialUserBuilder;

import org.junit.Test;

import com.google.common.collect.Sets;

public class TagsTest {
    @Test
    public void getConnectedGroupTag() throws Exception {
        SocialUser owner = new SocialUserBuilder().withProviderType(ProviderType.facebook).build();
        Tag java1 = aTag().withName("java").withPooled(true).build();
        Tag connectedTag = aTag()
                .withName("SLiPP")
                .withPooled(true)
                .withOwner(owner)
                .withGroupId("1234567")
                .build();
        Set<Tag> tagSet = Sets.newHashSet(java1, connectedTag);
        Tags tags = new Tags(tagSet);
        Tag actual = tags.getConnectedGroupTag();
        assertThat(actual, is(connectedTag));
    }
    
    @Test
    public void getConnectedGroupTag_사용자_추가요청_태그는_있으나_승인전() throws Exception {
        SocialUser owner = new SocialUserBuilder().withProviderType(ProviderType.facebook).build();
        Tag java1 = aTag().withName("java").withPooled(true).build();
        Tag connectedTag = aTag()
                .withName("SLiPP")
                .withPooled(false)
                .withOwner(owner)
                .withGroupId("1234567")
                .build();
        Set<Tag> tagSet = Sets.newHashSet(java1, connectedTag);
        Tags tags = new Tags(tagSet);
        Tag actual = tags.getConnectedGroupTag();
        assertThat(actual, is(nullValue()));
    }
    
    @Test
    public void getConnectedGroupTagHasNotConntectedGroup() throws Exception {
        Tag java1 = aTag().withName("java").withPooled(true).build();
        Tag c = aTag().withName("c").withPooled(false).build();

        Set<Tag> tagSet = Sets.newHashSet(java1, c);
        Tags tags = new Tags(tagSet);
        Tag actual = tags.getConnectedGroupTag();
        assertThat(actual, is(nullValue()));
    }
    
    @Test
    public void getConnectedGroupTagEmptyTag() throws Exception {
        Set<Tag> tagSet = Sets.newHashSet();
        Tags tags = new Tags(tagSet);
        Tag actual = tags.getConnectedGroupTag();
        assertThat(actual, is(nullValue()));
    }    
}
