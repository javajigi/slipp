package net.slipp.service.user;

import org.apache.commons.lang.RandomStringUtils;

public class RandomPasswordGenerator implements PasswordGenerator {
    private static final int DEFAULT_RANDOM_PASSWORD_LENGTH = 12;
    
    @Override
    public String generate() {
        return RandomStringUtils.randomAlphanumeric(DEFAULT_RANDOM_PASSWORD_LENGTH);
    }
}
