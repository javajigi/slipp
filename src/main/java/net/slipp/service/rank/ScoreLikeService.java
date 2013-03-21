package net.slipp.service.rank;

import net.slipp.domain.qna.ScoreLike;
import net.slipp.domain.qna.ScoreLikeType;
import net.slipp.repository.qna.ScoreLikeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 향후 질문(Quest)와의 동적 처리
 * 
 * @author eclipse4j
 * 
 */
@Service
public class ScoreLikeService {

	@Autowired
	private ScoreLikeRepository scoreLikeRepository;

	public boolean alreadyLikedAnswer(Long answerId, Long socialUserId) {
		ScoreLike scoreLike = scoreLikeRepository.findBySocialUserIdAnds(ScoreLikeType.ANSWER, answerId,socialUserId);
		if (scoreLike == null) {
			return false;
		}
		return true;
	}

	public void saveLikeAnswer(Long answerId, Long socialUserId) {
		scoreLikeRepository.save(new ScoreLike(ScoreLikeType.ANSWER, socialUserId, answerId));
	}
}
