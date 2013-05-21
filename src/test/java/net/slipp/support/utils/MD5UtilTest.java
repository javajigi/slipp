package net.slipp.support.utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5UtilTest {
    private static Logger log = LoggerFactory.getLogger(MD5UtilTest.class);
    
    @Test
    public void md5hex() {
        String email = "someone@somewhere.com";
        String hash = MD5Util.md5Hex(email);
        log.debug("hash : {}", hash);
    }
}
