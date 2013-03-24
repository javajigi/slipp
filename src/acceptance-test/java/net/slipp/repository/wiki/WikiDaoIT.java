package net.slipp.repository.wiki;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import net.slipp.domain.wiki.WikiDao;
import net.slipp.domain.wiki.WikiPage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/infrastructure.xml")
public class WikiDaoIT {
	private static Logger logger = LoggerFactory.getLogger(WikiDaoIT.class);
	
	@Resource(name="wikiDataSource")
	private DataSource dataSource;
	
	private WikiDao dut;
	
	@Before
	public void setup() {
		dut = new WikiDao();
		dut.setDataSource(dataSource);
	}

	@Test
	public void findWikiPages() {
		List<WikiPage> wikiPages = dut.findWikiPages();
		logger.debug("page size : {}", wikiPages.size());
		for (WikiPage wikiPage : wikiPages) {
			logger.debug("pageId : {}", wikiPage.getPageId());
			logger.debug("title : {}", wikiPage.getTitle());
			logger.debug("contents : {}", wikiPage.getContents());
		}
	}

}
