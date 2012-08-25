package net.slipp.domain.wiki;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class WikiDao extends JdbcDaoSupport {
	public List<WikiPage> findWikiPages() {
		String sql = "SELECT c.CONTENTID, c.TITLE, c.CREATIONDATE, b.BODY  " +
				"FROM SPACES s, CONTENT c, BODYCONTENT b " +
				"WHERE s.SPACEID = c.SPACEID AND c.CONTENTID = b.CONTENTID " +
				"AND (s.SPACEKEY = 'slipp' OR s.SPACEKEY = 'programming') " + 
				"AND c.CONTENTTYPE='PAGE' " +
				"AND c.CONTENT_STATUS='current'" +
				"ORDER BY c.CREATIONDATE DESC LIMIT 5";
		
		RowMapper<WikiPage> rowMapper = new RowMapper<WikiPage>() {
			@Override
			public WikiPage mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				WikiPage wikiPage = new WikiPage(
						rs.getLong("CONTENTID"), rs.getString("TITLE"), rs.getTimestamp("CREATIONDATE"), rs.getString("BODY"));
				return wikiPage;
			}
		};
		
		
		return getJdbcTemplate().query(sql, rowMapper);
	}

}
