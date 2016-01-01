package net.slipp

import net.slipp.support.AbstractATTest
import org.junit.{Test, Before}

class AdminAT extends AbstractATTest {

  @Before override def setup() {
    super.setup()
    driver.get("http://localhost:8080")
  }

  private def goAdminPage() = {
    loginToFacebook(1)
    indexPage.verifyAdminPage()
    indexPage.goAdminPage
  }

  @Test def user_management() {
    val adminQuestionPage = goAdminPage()
    adminQuestionPage.goAdminUserPage()
  }

  @Test def question_management() {
    val adminQuestionPage = goAdminPage()

  }

  @Test def tag_management() {
    val adminQuestionPage = goAdminPage()
    adminQuestionPage.goAdminTagPage()
  }
}
