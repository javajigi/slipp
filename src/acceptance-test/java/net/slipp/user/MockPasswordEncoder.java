package net.slipp.user;

import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

public class MockPasswordEncoder implements PasswordEncoder {
    private PasswordEncoder encoder;

    public MockPasswordEncoder() {
        encoder = new ShaPasswordEncoder(256);
    }
    
    public String encodePassword(String rawPass) {
        return encoder.encodePassword(rawPass, null);
    }    

    @Override
    public String encodePassword(String rawPass, Object salt) {
        return encoder.encodePassword(rawPass, salt);
    }

    @Override
    public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
        return encoder.isPasswordValid(encPass, rawPass, salt);
    }
}
