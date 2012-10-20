package net.slipp.service.wiki;

import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.wiki.WikiDao;
import net.slipp.domain.wiki.WikiPage;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class WikiService {
	@Resource(name="wikiDao")
	private WikiDao wikiDao;
	
	@Cacheable(value="blogPages")
	public List<WikiPage> findWikiPages() {
		return wikiDao.findWikiPages();
	}
}
