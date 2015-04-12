package net.slipp.service.smalltalk;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import net.slipp.domain.smalltalk.SmallTalk;
import net.slipp.domain.smalltalk.SmallTalkComment;
import net.slipp.domain.summary.SiteSummary;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.smalltalk.SmallTalkCommentRepository;
import net.slipp.repository.smalltalk.SmallTalkRepository;
import net.slipp.service.summary.SummaryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service("smallTalkService")
@Transactional
public class SmallTalkService {

	private Logger logger = LoggerFactory.getLogger(SmallTalkService.class);

	@Resource (name = "smallTalkRepository")
	private SmallTalkRepository smallTalkRepository;
	
	@Resource (name = "smallTalkCommentRepository")
	private SmallTalkCommentRepository smallTalkCommentRepository;
	
	@Resource (name = "summaryService")
	private SummaryService summaryService;

	public void create(SmallTalk smallTalk) {
		logger.debug("SmallTalk : {}", smallTalk);
		smallTalkRepository.save(smallTalk);
	}

	public SmallTalkComment createComment(Long smallTalkId, SmallTalkComment smallTalkComment){
		SmallTalk smallTalk = smallTalkRepository.findOne(smallTalkId);
		smallTalkComment.commentTo(smallTalk);
		return smallTalkCommentRepository.save(smallTalkComment);
	}
	
	public List<SmallTalk> getLastTalks() {
		Page<SmallTalk> page = smallTalkRepository.findAll(getPager());
		List<SmallTalk> orgSmallTalks = page.getContent();
		
		List<SmallTalk> smallTalks = Lists.newArrayList();
		for (SmallTalk smallTalk : orgSmallTalks) {
		    if (smallTalk.hasUrl()) {
		        smallTalk.setSiteSummary(findSummary(smallTalk));
		    }
			smallTalks.add(smallTalk);
		}
		return smallTalks;
	}

	private SiteSummary findSummary(SmallTalk smallTalk) {
		return summaryService.findOneThumbnail(smallTalk.getUrlInTalk());
	}

	private PageRequest getPager() {
		return new PageRequest(0, 10, sortByIdDesc());
	}

	private Sort sortByIdDesc() {
		return new Sort(Sort.Direction.DESC, "smallTalkId");
	}

	public List<SmallTalkComment> findCommnetsBySmallTalkId(Long smallTalkId) {
		SmallTalk smallTalk = new SmallTalk();
		smallTalk.setSmallTalkId(smallTalkId);
		return smallTalkCommentRepository.findBySmallTalk(smallTalk);
	}

	public void deleteToBlock(SocialUser user) {
		List<SmallTalkComment> comments = smallTalkCommentRepository.findByWriter(user);
		deleteSmallTalkComments(comments);
		
		List<SmallTalk> smallTalks = smallTalkRepository.findByWriter(user);
		for (SmallTalk smallTalk : smallTalks) {
			List<SmallTalkComment> commentsAll = smallTalkCommentRepository.findBySmallTalk(smallTalk);
			deleteSmallTalkComments(commentsAll);
			smallTalkRepository.delete(smallTalk);
		}
	}

	private void deleteSmallTalkComments(List<SmallTalkComment> comments) {
		for (SmallTalkComment comment : comments) {
			smallTalkCommentRepository.delete(comment);
		}
	}
}
