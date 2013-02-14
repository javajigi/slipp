package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class SnsTypeTest {

    @Test
    public void valueof() {
        SnsType type = SnsType.valueOf("facebook");
        assertThat(type, is(SnsType.facebook));
    }

}
