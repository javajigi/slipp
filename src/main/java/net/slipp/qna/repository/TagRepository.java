package net.slipp.qna.repository;

import net.slipp.qna.domain.Tag;

import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Long>{
	Tag findByName(String name);
}
