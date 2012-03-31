package net.slipp.domain.qna;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import net.slipp.repository.qna.TagRepository;

import org.springframework.stereotype.Component;

@Component
public class TagPopulator {
	@Resource (name = "tagRepository")
	private TagRepository tagRepository;
	
	@PostConstruct
	public void populate() {
		tagRepository.save(new Tag("java"));
		tagRepository.save(new Tag("javascript"));
		tagRepository.save(new Tag("jquery"));
		tagRepository.save(new Tag("eclipse"));
		tagRepository.save(new Tag("html"));
		tagRepository.save(new Tag("oop"));
		tagRepository.save(new Tag("ant"));
		tagRepository.save(new Tag("maven"));
		tagRepository.save(new Tag("svn"));
		tagRepository.save(new Tag("git"));
		tagRepository.save(new Tag("cvs"));
		tagRepository.save(new Tag("sitemash"));
		tagRepository.save(new Tag("junit"));
		tagRepository.save(new Tag("guava"));
		tagRepository.save(new Tag("jpa"));
		tagRepository.save(new Tag("hibernate"));
		tagRepository.save(new Tag("orm"));
	}
}
