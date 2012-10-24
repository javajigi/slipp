package net.slipp.repository.tag;

import java.util.List;

import net.slipp.domain.tag.Tag;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TagRepository extends PagingAndSortingRepository<Tag, Long>{
	Tag findByName(String name);

	List<Tag> findByNameLike(String name);

	@Query("SELECT t FROM Tag t WHERE t.parent IS NULL ORDER BY t.taggedCount DESC, t.tagId ASC")
	List<Tag> findParents();
}
