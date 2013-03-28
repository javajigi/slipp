package net.slipp.service.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class FacebookServiceTest {

    @Test
    public void createLink_to_question() {
        final String url = "http://localhost:8080";
        FacebookService dut = new FacebookService() {
            @Override
            protected String createApplicationUrl() {
                return url;
            }
            
        };
        
        assertThat(dut.createLink(1L), is(url + "/questions/1"));
    }

    
    @Test
    public void createLink_to_answer() {
        final String url = "http://localhost:8080";
        FacebookService dut = new FacebookService() {
            @Override
            protected String createApplicationUrl() {
                return url;
            }
            
        };
        
        assertThat(dut.createLink(1L, 2L), is(url + "/questions/1#answer-2"));
    }
}
