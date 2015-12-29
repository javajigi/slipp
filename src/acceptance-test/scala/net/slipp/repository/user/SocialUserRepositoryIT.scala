package net.slipp.repository.user

import net.slipp.domain.user.SocialUser
import net.slipp.domain.user.SocialUserBuilder
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import slipp.config.PersistenceJPAConfig

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[PersistenceJPAConfig]))
class SocialUserRepositoryIT {
  @Autowired private var socialUserRepository: SocialUserRepository = null

  @Test
  @throws(classOf[Exception])
  def save {
    val socialUser: SocialUser = SocialUserBuilder.aSocialUser.createTestUser("javajigi")
    socialUserRepository.save(socialUser)
  }
}
