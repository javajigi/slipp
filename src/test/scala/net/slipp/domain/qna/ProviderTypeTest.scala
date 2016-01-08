package net.slipp.domain.qna

import net.slipp.domain.ProviderType
import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import org.junit.Test

class ProviderTypeTest {
  @Test def valueof {
    val `type`: ProviderType = ProviderType.valueOf("facebook")
    assertThat(`type`, is(ProviderType.facebook))
  }
}
