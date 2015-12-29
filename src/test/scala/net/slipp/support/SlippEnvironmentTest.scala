package net.slipp.support

import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.junit.Test

class SlippEnvironmentTest {
  @Test def loadProperties {
    val environment: SlippEnvironment = new SlippEnvironment
    assertThat(environment.getProperty("environment"), is("DEVELOPMENT"))
  }
}
