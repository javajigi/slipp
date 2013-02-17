package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class SnsConnectionTest {

	@Test
	public void isConnection() {
		SnsConnection connection = new SnsConnection();
		assertThat(connection.isConnected(), is(false));
		
		connection = new SnsConnection(SnsType.facebook, "123456");
		assertThat(connection.isConnected(), is(true));
	}

}
