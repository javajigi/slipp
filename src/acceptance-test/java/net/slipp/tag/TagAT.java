package net.slipp.tag;

import net.slipp.support.AbstractATTest;

import org.junit.Before;
import org.junit.Test;

public class TagAT extends AbstractATTest {
    @Before
    public void setup() {
        super.setup();
        driver.get("http://localhost:8080");
    }
    
    @Test
    public void request_add_tag() throws Exception {
        loginToFacebook(1);
        RequestTagPage requestTagPage = indexPage.goRequestTagPage();
    }
    
    
}
