package net.slipp.domain.tag

import net.slipp.support.AbstractATTest
import org.junit.Before
import org.junit.Test

class TagAT extends AbstractATTest {
  @Before override def setup {
    super.setup
    driver.get("http://localhost:8080")
  }

  @Test
  def request_add_tag {
    loginToFacebook(1)
    val requestTagPage: RequestTagPage = indexPage.goRequestTagPage
  }
}
