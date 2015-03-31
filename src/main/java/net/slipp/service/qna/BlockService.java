package net.slipp.service.qna;

import javax.annotation.Resource;

import net.slipp.domain.user.SocialUser;
import net.slipp.service.smalltalk.SmallTalkService;
import net.slipp.service.user.SocialUserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("blockService")
@Transactional
public class BlockService {
	@Resource(name = "socialUserService")
	private SocialUserService socialUserService;
	
	@Resource(name = "qnaService")
	private QnaService qnaService;
	
	@Resource(name = "smallTalkService")
	private SmallTalkService smallTalkService;

	public void block(Long id) {
		SocialUser user = socialUserService.findById(id);
		user.blocked();
		qnaService.deleteToBlock(user);
		smallTalkService.deleteToBlock(user);
	}

}
