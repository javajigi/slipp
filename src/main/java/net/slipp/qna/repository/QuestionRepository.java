package net.slipp.qna.repository;

import net.slipp.qna.domain.Question;

import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Long>{

}
