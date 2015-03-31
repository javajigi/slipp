package net.slipp.repository.qna;

import java.util.List;

import net.slipp.domain.qna.Answer;
import net.slipp.domain.user.SocialUser;
import net.slipp.support.jpa.SlippCommonRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnswerRepository extends SlippCommonRepository<Answer, Long> {
	List<Answer> findByWriter(SocialUser writer);

	@Query("select a from Answer a where a.writer = :writer group by a.question")
    Page<Answer> findsAnswerByWriter(@Param("writer") SocialUser writer, Pageable pageable);
}