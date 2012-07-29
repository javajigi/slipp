package net.slipp.repository.qna;

import net.slipp.domain.qna.NewTag;

import org.springframework.data.repository.CrudRepository;

public interface NewTagRepository extends CrudRepository<NewTag, Long>{
	NewTag findByName(String name);
}
