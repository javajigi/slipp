package net.slipp.repository.tag;

import java.util.List;

import net.slipp.domain.tag.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface TagRepository extends PagingAndSortingRepository<Tag, Long>{
	Tag findByName(String name);
	
	@Query("SELECT t FROM Tag t WHERE t.tagInfo.groupId = :groupId")
	Tag findByGroupId(@Param("groupId") String groupId);

	List<Tag> findByNameLike(String name);

	@Query("SELECT t FROM Tag t WHERE t.parent IS NULL ORDER BY t.taggedCount DESC, t.tagId ASC")
	List<Tag> findPooledParents();

	@Query("SELECT t FROM Tag t ORDER BY t.taggedCount DESC, t.tagId ASC")
	Page<Tag> findByPooledTag(Pageable page);
}
