package net.slipp.repository.tag;

import net.slipp.domain.tag.TaggedHistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TaggedHistoryRepository extends CrudRepository<TaggedHistory, Long> {
	@Query("SELECT th.tagId, MAX(th.historyId) as highestId from TaggedHistory th " + 
			"WHERE th.taggedType = 'TAGGED'" +
			"group by th.tagId order by highestId desc")
	Page<Object[]> findsLatest(Pageable pageable);
}
