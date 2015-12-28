package net.slipp.service.tag

import javax.annotation.PostConstruct
import javax.annotation.Resource
import net.slipp.domain.tag.Tag
import net.slipp.repository.tag.TagRepository
import org.springframework.stereotype.Component

@Component class TagPopulator {
  @Resource(name = "tagRepository") private var tagRepository: TagRepository = null

  @PostConstruct def populate {
    if (tagRepository.findByName("java") == null) {
      val 스터디: Tag = tagRepository.save(Tag.pooledTag("스터디"))
      tagRepository.save(Tag.pooledTag("study", 스터디))
      val java: Tag = tagRepository.save(Tag.pooledTag("java"))
      tagRepository.save(Tag.pooledTag("자바", java))
      tagRepository.save(Tag.pooledTag("jdk", java))
      tagRepository.save(Tag.pooledTag("jre", java))
      val servlet: Tag = tagRepository.save(Tag.pooledTag("servlet"))
      tagRepository.save(Tag.pooledTag("서블릿", servlet))
      tagRepository.save(Tag.pooledTag("jsp"))
      val spring: Tag = tagRepository.save(Tag.pooledTag("spring"))
      tagRepository.save(Tag.pooledTag("스프링", spring))
      tagRepository.save(Tag.pooledTag("junit"))
      tagRepository.save(Tag.pooledTag("jpa"))
      val hibernate: Tag = tagRepository.save(Tag.pooledTag("hibernate"))
      tagRepository.save(Tag.pooledTag("하이버네이트", hibernate))
      tagRepository.save(Tag.pooledTag("mybatis"))
      val web: Tag = tagRepository.save(Tag.pooledTag("web"))
      tagRepository.save(Tag.pooledTag("웹", web))
      val javascript: Tag = tagRepository.save(Tag.pooledTag("javascript"))
      tagRepository.save(Tag.pooledTag("jquery"))
      tagRepository.save(Tag.pooledTag("자바스크립트", javascript))
      val eclipse: Tag = tagRepository.save(Tag.pooledTag("eclipse"))
      tagRepository.save(Tag.pooledTag("이클립스", eclipse))
      val maven: Tag = tagRepository.save(Tag.pooledTag("maven"))
      tagRepository.save(Tag.pooledTag("메이븐", maven))
      tagRepository.save(Tag.pooledTag("sql"))
      val database: Tag = tagRepository.save(Tag.pooledTag("database"))
      tagRepository.save(Tag.pooledTag("데이터베이스", database))
      tagRepository.save(Tag.pooledTag("db", database))
      val agile: Tag = tagRepository.save(Tag.pooledTag("agile"))
      tagRepository.save(Tag.pooledTag("애자일", agile))
      tagRepository.save(Tag.pooledTag("git"))
      val svn: Tag = tagRepository.save(Tag.pooledTag("svn"))
      tagRepository.save(Tag.pooledTag("subversion", svn))
      val continuousIntegration: Tag = tagRepository.save(Tag.pooledTag("continuous-integration"))
      tagRepository.save(Tag.pooledTag("ci", continuousIntegration))
      tagRepository.save(Tag.pooledTag("지속적통합", continuousIntegration))
    }
  }
}
