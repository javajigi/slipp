package net.slipp.repository.qna;

import net.slipp.domain.qna.Question;
import net.slipp.support.jpa.SlippCommonRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends SlippCommonRepository<Question, Long>{

	@Query("SELECT q from Question q JOIN q.tags t where t.name = :name")
	Page<Question> findsByTag(@Param("name") String name, Pageable pageable);

}
