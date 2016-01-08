package net.slipp.repository.tag

import java.lang.Long
import net.slipp.domain.tag.TaggedHistory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

trait TaggedHistoryRepository extends CrudRepository[TaggedHistory, Long] {
  @Query("SELECT th.tagId, MAX(th.historyId) as highestId from TaggedHistory th "
    + "WHERE th.taggedType = 'TAGGED'" + "group by th.tagId order by highestId desc")
  def findsLatest(pageable: Pageable): Page[Array[AnyRef]]
}
