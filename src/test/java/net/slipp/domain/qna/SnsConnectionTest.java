package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import net.slipp.domain.ProviderType;

import org.junit.Test;

public class SnsConnectionTest {

	@Test
	public void isConnection() {
		SnsConnection connection = new SnsConnection();
		assertThat(connection.isConnected(), is(false));
		
		connection = new SnsConnection(ProviderType.facebook, "123456");
		assertThat(connection.isConnected(), is(true));
	}
	
	@Test
    public void updateAnswerCount() throws Exception {
	    SnsConnection connection = new SnsConnection();
	    int answerCount = 5;
	    connection.updateAnswerCount(answerCount);
	    assertThat(connection.getSnsAnswerCount(), is(answerCount));
    }

}
