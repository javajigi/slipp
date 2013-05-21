package net.slipp.service.user;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class PasswordGeneratorTest {
    @Test
    public void randomGenerate() throws Exception {
        PasswordGenerator dut = new RandomPasswordGenerator();
        String first = dut.generate();
        String second = dut.generate();
        assertThat(first.equals(second), is(false));
    }
    
    @Test
    public void fixedGenerate_passPassword() throws Exception {
        String password = "password";
        PasswordGenerator dut = new FixedPasswordGenerator(password);
        assertThat(dut.generate(), is(password));
    }
    
    @Test
    public void fixedGenerate_null() throws Exception {
        String password = "password";
        PasswordGenerator dut = new FixedPasswordGenerator();
        assertThat(dut.generate(), is(password));
    }
}