package net.slipp.domain.qna

import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import net.slipp.domain.ProviderType
import org.junit.Test

class SnsConnectionTest {
  @Test def isConnection {
    var connection: SnsConnection = new SnsConnection
    assertThat(connection.isConnected, is(false))
    connection = new SnsConnection(ProviderType.facebook, "123456")
    assertThat(connection.isConnected, is(true))
  }

  @Test
  @throws(classOf[Exception])
  def updateAnswerCount {
    val connection: SnsConnection = new SnsConnection
    val answerCount: Int = 5
    connection.updateAnswerCount(answerCount)
    assertThat(connection.getSnsAnswerCount, is(answerCount))
  }

  @Test
  @throws(classOf[Exception])
  def removeId {
    assertThat(SnsConnection.removeId("1324855987_390710267709840"), is("390710267709840"))
  }

  @Test
  @throws(classOf[Exception])
  def removeId_noId {
    assertThat(SnsConnection.removeId(" "), is(" "))
    assertThat(SnsConnection.removeId("390710267709840"), is("390710267709840"))
  }
}
