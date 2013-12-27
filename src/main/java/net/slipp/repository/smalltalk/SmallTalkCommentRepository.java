package net.slipp.repository.smalltalk;

import java.util.List;

import net.slipp.domain.smalltalk.SmallTalk;
import net.slipp.domain.smalltalk.SmallTalkComment;
import net.slipp.support.jpa.SlippCommonRepository;

public interface SmallTalkCommentRepository extends SlippCommonRepository<SmallTalkComment, Long> {
	List<SmallTalkComment> findBySmallTalk(SmallTalk smallTalk);
}