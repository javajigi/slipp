package net.slipp.service.smalltalk;

import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.smalltalk.SmallTalk;
import net.slipp.repository.smalltalk.SmallTalkRepository;
import net.slipp.service.summary.SummaryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service("smallTalkService")
public class SmallTalkService {

	private Logger logger = LoggerFactory.getLogger(SmallTalkService.class);

	@Resource (name = "smallTalkRepository")
	private SmallTalkRepository smallTalkRepository;
	
	@Resource (name = "summaryService")
	private SummaryService summaryService;

	@CacheEvict(value="smallTalkCache", allEntries=true)
	public void save(SmallTalk smallTalk) {
		logger.debug("SmallTalk : {}", smallTalk);
		smallTalkRepository.save(smallTalk);
	}

	public List<SmallTalk> getLastTalks() {
		Page<SmallTalk> page = smallTalkRepository.findAll(getPager());
		List<SmallTalk> orgSmallTalks = page.getContent();
		
		List<SmallTalk> smallTalks = Lists.newArrayList();
		for (SmallTalk smallTalk : orgSmallTalks) {
		    if (smallTalk.hasUrl()) {
		    	long start = System.currentTimeMillis();
		        smallTalk.setSiteSummary(summaryService.findOneThumbnail(smallTalk.getUrlInTalk()));
		        long end = System.currentTimeMillis();
		        logger.debug("execution time : {}", (end-start));
		    }
			smallTalks.add(smallTalk);
		}
		return smallTalks;
	}

	private PageRequest getPager() {
		return new PageRequest(0, 10, sortByIdDesc());
	}

	private Sort sortByIdDesc() {
		return new Sort(Sort.Direction.DESC, "smallTalkId");
	}
}
