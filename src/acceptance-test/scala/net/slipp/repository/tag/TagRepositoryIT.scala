package net.slipp.repository.tag

import javax.transaction.Transactional

import net.slipp.domain.tag.Tag
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import slipp.config.PersistenceJPAConfig

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[PersistenceJPAConfig]))
class TagRepositoryIT {
  @Autowired private var tagRepository: TagRepository = null

  @Test def findParents {
    val parent1: Tag = Tag.pooledTag("parent1")
    tagRepository.save(parent1)
    tagRepository.save(Tag.pooledTag("child1", parent1))
    tagRepository.save(Tag.pooledTag("child2", parent1))
    tagRepository.save(Tag.pooledTag("parent2"))
    val tags = tagRepository.findPooledParents
    assertThat(tags.size, is(2))
  }
}
