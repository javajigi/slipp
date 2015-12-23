package net.slipp.social.security;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class PasswordEncoderTest {
    private static final Logger logger = LoggerFactory.getLogger(PasswordEncoderTest.class);

    @Test
    public void oldNNewPasswordEncoder() {
        ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(256);
        String result = passwordEncoder.encodePassword("password", "");
        logger.debug("encoded password : {}, length : {}", result, result.length());
    }

    @Test
    public void bcryptPasswordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String result = passwordEncoder.encode("password");
        logger.debug("encoded password : {}, length : {}", result, result.length());
    }
}
