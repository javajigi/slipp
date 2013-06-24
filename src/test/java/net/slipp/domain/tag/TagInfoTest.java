package net.slipp.domain.tag;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import net.slipp.domain.user.SocialUser;

import org.junit.Test;

public class TagInfoTest {
    @Test
    public void isRequestedTag() {
        SocialUser owner = new SocialUser(10);
        TagInfo dut = new TagInfo(owner, "1234", "description");
        assertThat(dut.isRequestedTag(), is(true));
    }
    
    @Test
    public void isNotRequestedTag() throws Exception {
        TagInfo dut = new TagInfo();
        assertThat(dut.isRequestedTag(), is(false));        
    }
    
    @Test
    public void isConnectGroup() throws Exception {
        SocialUser owner = new SocialUser(10);
        TagInfo dut = new TagInfo(owner, "1234", "description");
        assertThat(dut.isConnectGroup(), is(true));        
    }
    
    @Test
    public void isNotConnectGroup() throws Exception {
        TagInfo dut = new TagInfo();
        assertThat(dut.isConnectGroup(), is(false));        
    }
    
    @Test
    public void getGroupUrl() throws Exception {
        SocialUser owner = new SocialUser(10);
        TagInfo dut = new TagInfo(owner, "1234", "description");
        assertThat(dut.getGroupUrl(), is("https://www.facebook.com/groups/1234"));        
    }
    
    @Test
    public void getGroupUrlGroupIdIsEmpty() throws Exception {
        TagInfo dut = new TagInfo();
        assertThat(dut.getGroupUrl(), is(""));        
    }
}
