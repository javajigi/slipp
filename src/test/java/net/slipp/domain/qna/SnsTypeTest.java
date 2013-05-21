package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import net.slipp.domain.ProviderType;

import org.junit.Test;

public class SnsTypeTest {

    @Test
    public void valueof() {
        ProviderType type = ProviderType.valueOf("facebook");
        assertThat(type, is(ProviderType.facebook));
    }

}
