package net.slipp.domain.tag;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TagInfoTest {
    @Test
    public void isConnectGroup() throws Exception {
        TagInfo dut = new TagInfo("1234", "description");
        assertThat(dut.isConnectGroup(), is(true));        
    }
    
    @Test
    public void isNotConnectGroup() throws Exception {
        TagInfo dut = new TagInfo();
        assertThat(dut.isConnectGroup(), is(false));        
    }
    
    @Test
    public void getGroupUrl() throws Exception {
        TagInfo dut = new TagInfo("1234", "description");
        assertThat(dut.getGroupUrl(), is("https://www.facebook.com/groups/1234"));        
    }
    
    @Test
    public void getGroupUrlGroupIdIsEmpty() throws Exception {
        TagInfo dut = new TagInfo();
        assertThat(dut.getGroupUrl(), is(""));        
    }
}
