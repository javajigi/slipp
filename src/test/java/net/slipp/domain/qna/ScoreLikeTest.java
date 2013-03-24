package net.slipp.domain.qna;

import net.slipp.domain.user.SocialUser;
import net.slipp.repository.qna.ScoreLikeRepository;
import net.slipp.service.rank.ScoreLikeService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScoreLikeTest {
	private Answer answer;
	@InjectMocks
	private ScoreLikeService scoreLikeService = new ScoreLikeService();
	
	@Mock
	private ScoreLikeRepository scoreLikeRepository;

	@Before
	public void before() {
		answer = new Answer(1L);
	}

	@Test
	public void test_랭킹점수정보저장() {
		answer.writedBy(new SocialUser(1L));
		scoreLikeService.saveLikeAnswer(1L, 1L);
	}
}
