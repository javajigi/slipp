package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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
	
	@Test
	public void removeId() throws Exception {
		assertThat(SnsConnection.removeId("1324855987_390710267709840"), is("390710267709840"));
	}
	
	@Test
	public void removeId_noId() throws Exception {
		assertThat(SnsConnection.removeId(" "), is(" "));
		assertThat(SnsConnection.removeId("390710267709840"), is("390710267709840"));
	}

}
