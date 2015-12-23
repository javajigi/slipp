package net.slipp.social.security;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Sha256ToBcryptPasswordEncoderTest {
    private static final Logger logger = LoggerFactory.getLogger(Sha256ToBcryptPasswordEncoderTest.class);

    private Sha256ToBCryptPasswordEncoder sha256ToBCryptPasswordEncoder;

    @Before
    public void setup() {
        sha256ToBCryptPasswordEncoder = new Sha256ToBCryptPasswordEncoder();
        sha256ToBCryptPasswordEncoder.setBcryptPasswordEncoder(new BCryptPasswordEncoder());
        sha256ToBCryptPasswordEncoder.setSha256PasswordEncoder(new ShaPasswordEncoder(256));
    }

    @Test
    public void shaPasswordEncoder() {
        ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(256);
        String result = passwordEncoder.encodePassword("password", "");
        logger.debug("encoded password : {}, length : {}", result, result.length());
        assertTrue(sha256ToBCryptPasswordEncoder.matches("password", result));
    }

    @Test
    public void bcryptPasswordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String result = passwordEncoder.encode("password");
        logger.debug("encoded password : {}, length : {}", result, result.length());
        assertTrue(sha256ToBCryptPasswordEncoder.matches("password", result));
    }
}
