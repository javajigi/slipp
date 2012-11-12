package net.slipp.service.rank;

import net.slipp.domain.qna.Answer;
import net.slipp.domain.qna.ScoreLike;
import net.slipp.domain.qna.ScoreLikeType;
import net.slipp.repository.qna.ScoreLikeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreLikeService {

	@Autowired
	private ScoreLikeRepository scoreLikeRepository;

	public ScoreLike save(Answer answer) {
		return scoreLikeRepository
				.save(new ScoreLike(ScoreLikeType.ANSWER, answer.getWriter().getId(), answer.getAnswerId()));
	}
}
