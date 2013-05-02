package net.slipp.service.user;

public class FixedPasswordGenerator implements PasswordGenerator {
    public static final String DEFAULT_FIXED_PASSWORD = "password";
    
    private String password;
    
    public FixedPasswordGenerator() {
        this.password = DEFAULT_FIXED_PASSWORD;
    }

    public FixedPasswordGenerator(String password) {
        this.password = password;
    }

    @Override
    public String generate() {
        return password;
    }
}
