package net.slipp.repository.qna;

import net.slipp.domain.qna.Question;

import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Long>{

}
