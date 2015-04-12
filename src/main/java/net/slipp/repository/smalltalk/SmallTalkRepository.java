package net.slipp.repository.smalltalk;

import java.util.List;

import net.slipp.domain.smalltalk.SmallTalk;
import net.slipp.domain.user.SocialUser;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface SmallTalkRepository extends PagingAndSortingRepository<SmallTalk, Long>{
	List<SmallTalk> findByWriter(SocialUser writer);
}