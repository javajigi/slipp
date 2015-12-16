package net.slipp.service.qna

import javax.annotation.Resource

import net.slipp.service.smalltalk.SmallTalkService
import net.slipp.service.user.SocialUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("blockService")
@Transactional
class BlockService @Autowired() (
  @Resource(name = "socialUserService") socialUserService: SocialUserService,
  @Resource(name = "qnaService") qnaService: QnaService,
  @Resource(name = "smallTalkService") smallTalkService: SmallTalkService) {

  def block(id: Long) {
    val user = socialUserService.findById(id);
    user.blocked();
    qnaService.deleteToBlock(user);
    smallTalkService.deleteToBlock(user);
  }
}
