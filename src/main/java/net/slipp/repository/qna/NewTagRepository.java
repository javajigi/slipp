package net.slipp.repository.qna;

import net.slipp.domain.qna.NewTag;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface NewTagRepository extends PagingAndSortingRepository<NewTag, Long>{
	NewTag findByName(String name);
}
