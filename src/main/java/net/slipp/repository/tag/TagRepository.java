package net.slipp.repository.tag;

import java.util.List;

import net.slipp.domain.tag.Tag;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface TagRepository extends PagingAndSortingRepository<Tag, Long>{
	Tag findByName(String name);

	List<Tag> findByNameLike(String name);
}
