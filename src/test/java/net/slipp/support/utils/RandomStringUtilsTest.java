package net.slipp.support.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomStringUtilsTest {
    private static Logger log = LoggerFactory.getLogger(RandomStringUtilsTest.class);

    @Test
    public void generateRandomPassword() throws Exception {
        log.debug("password : {}", RandomStringUtils.randomAlphanumeric(12));
    }
}
