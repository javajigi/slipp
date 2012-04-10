package net.slipp.repository.qna;

import java.util.List;

import net.slipp.domain.qna.Tag;

import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Long>{
	Tag findByName(String name);

	List<Tag> findByNameLike(String name);
}
