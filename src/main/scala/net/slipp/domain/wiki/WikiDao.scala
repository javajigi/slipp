package net.slipp.domain.wiki

import java.sql.ResultSet
import java.util.List

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.support.JdbcDaoSupport

class WikiDao extends JdbcDaoSupport {
  def findWikiPages: List[WikiPage] = {
    val sql = "SELECT c.CONTENTID, c.TITLE, c.LASTMODDATE, b.BODY  FROM SPACES s, CONTENT c, BODYCONTENT b WHERE s.SPACEID = c.SPACEID AND c.CONTENTID = b.CONTENTID AND s.SPACEKEY = 'SLS' AND c.CONTENTTYPE='PAGE' AND c.CONTENT_STATUS='current' ORDER BY c.LASTMODDATE DESC LIMIT 5"

    val rowMapper: RowMapper[WikiPage] = new RowMapper[WikiPage]() {
      def mapRow(rs: ResultSet, rowNum: Int): WikiPage = {
        val wikiPage: WikiPage = new WikiPage(rs.getLong("CONTENTID"), rs.getString("TITLE"), rs.getTimestamp("LASTMODDATE"), rs.getString("BODY"))
        return wikiPage
      }
    }
    return getJdbcTemplate.query(sql, rowMapper)
  }
}
