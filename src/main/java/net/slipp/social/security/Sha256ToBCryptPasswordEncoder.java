package net.slipp.social.security;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Sha256ToBCryptPasswordEncoder implements PasswordEncoder {
    private PasswordEncoder bcryptPasswordEncoder;
    private MessageDigestPasswordEncoder sha256PasswordEncoder;

    @Override
    public String encode(CharSequence rawPassword) {
        return bcryptPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword.startsWith("$2a$10$") && encodedPassword.length() == 60) {
            return bcryptPasswordEncoder.matches(rawPassword, encodedPassword);
        }

        if (encodedPassword.length() == 64) {
            return sha256PasswordEncoder.isPasswordValid(encodedPassword, rawPassword.toString(), "");
        }

        return false;
    }

    public void setBcryptPasswordEncoder(PasswordEncoder bcryptPasswordEncoder) {
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    }

    public void setSha256PasswordEncoder(MessageDigestPasswordEncoder sha256PasswordEncoder) {
        this.sha256PasswordEncoder = sha256PasswordEncoder;
    }
}
