package net.slipp.repository.tag;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext.xml")
public class TaggedHistoryRepositoryTest {
	private static final Logger log = LoggerFactory.getLogger(TaggedHistoryRepositoryTest.class);
	
	@Autowired
	private TaggedHistoryRepository taggedHistoryRepository;

	@Test
	public void findsLatest() {
		PageRequest page = new PageRequest(0, 10);
		Page<Object[]> latestTags = taggedHistoryRepository.findsLatest(page);
		for (Object[] tagId : latestTags.getContent()) {
			log.debug("TagId : {}", tagId[0]);
		}
	}
}
