package net.slipp.repository.qna;

import net.slipp.domain.qna.Answer;
import net.slipp.domain.user.SocialUser;
import net.slipp.support.jpa.SlippCommonRepository;

import java.util.List;

public interface AnswerRepository extends SlippCommonRepository<Answer, Long> {
	List<Answer> findByWriter(SocialUser writer);
}