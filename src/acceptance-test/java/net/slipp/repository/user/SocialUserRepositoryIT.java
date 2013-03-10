package net.slipp.repository.user;

import net.slipp.domain.user.SocialUser;
import net.slipp.domain.user.SocialUserBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-applicationContext.xml")
public class SocialUserRepositoryIT {
	@Autowired
	private SocialUserRepository socialUserRepository;
	
	@Test
	public void save() throws Exception {
		SocialUser socialUser = SocialUserBuilder.aSocialUser().createTestUser("javajigi");
		socialUserRepository.save(socialUser);
	}
}
