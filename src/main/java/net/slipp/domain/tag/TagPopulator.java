package net.slipp.domain.tag;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import net.slipp.repository.tag.TagRepository;

import org.springframework.stereotype.Component;

@Component
public class TagPopulator {
	@Resource (name = "tagRepository")
	private TagRepository tagRepository;
	
	@PostConstruct
	public void populate() {
		if (tagRepository.findByName("java") == null) {
			Tag 스터디 = tagRepository.save(new Tag("스터디"));
			tagRepository.save(new Tag("study", 스터디));
			Tag java = tagRepository.save(new Tag("java"));
			tagRepository.save(new Tag("자바", java));
			tagRepository.save(new Tag("jdk", java));
			tagRepository.save(new Tag("jre", java));
			Tag servlet = tagRepository.save(new Tag("servlet"));
			tagRepository.save(new Tag("서블릿", servlet));
			tagRepository.save(new Tag("jsp"));
			Tag spring = tagRepository.save(new Tag("spring"));
			tagRepository.save(new Tag("스프링", spring));
			tagRepository.save(new Tag("junit"));
			tagRepository.save(new Tag("jpa"));
			Tag hibernate = tagRepository.save(new Tag("hibernate"));
			tagRepository.save(new Tag("하이버네이트", hibernate));
			tagRepository.save(new Tag("mybatis"));
			Tag javascript = tagRepository.save(new Tag("javascript"));
			tagRepository.save(new Tag("jquery"));
			tagRepository.save(new Tag("자바스크립트", javascript));
			Tag eclipse = tagRepository.save(new Tag("eclipse"));
			tagRepository.save(new Tag("이클립스", eclipse));
			Tag maven = tagRepository.save(new Tag("maven"));
			tagRepository.save(new Tag("메이븐", maven));
			tagRepository.save(new Tag("sql"));
			Tag database = tagRepository.save(new Tag("database"));
			tagRepository.save(new Tag("데이터베이스", database));
			tagRepository.save(new Tag("db", database));
			Tag agile = tagRepository.save(new Tag("agile"));
			tagRepository.save(new Tag("애자일", agile));
			tagRepository.save(new Tag("git"));
			Tag svn = tagRepository.save(new Tag("svn"));
			tagRepository.save(new Tag("subversion", svn));
			Tag continuousIntegration = tagRepository.save(new Tag("continuous-integration"));
			tagRepository.save(new Tag("ci", continuousIntegration));
			tagRepository.save(new Tag("지속적통합", continuousIntegration));
		}
	}
}
