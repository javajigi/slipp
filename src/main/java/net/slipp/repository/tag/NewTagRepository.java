package net.slipp.repository.tag;

import net.slipp.domain.tag.NewTag;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface NewTagRepository extends PagingAndSortingRepository<NewTag, Long>{
	NewTag findByName(String name);
}
