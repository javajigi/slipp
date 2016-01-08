package net.slipp.service.tag

import com.google.common.collect.Sets
import net.slipp.domain.fb.FacebookGroup
import net.slipp.domain.tag.Tag
import net.slipp.domain.tag.TagBuilder.aTag
import net.slipp.repository.tag.TagRepository
import org.hamcrest.CoreMatchers.is
import org.junit.Assert._
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.when
import org.mockito.runners.MockitoJUnitRunner
import org.mockito.{InjectMocks, Mock}
import org.slf4j.LoggerFactory

@RunWith(classOf[MockitoJUnitRunner])
class TagServiceTest {
  private val log = LoggerFactory.getLogger(classOf[TagServiceTest])

  @Mock private var tagRepository: TagRepository = null
  @InjectMocks private val dut = new TagService

  @Test def moveToTag_부모_태그_ID_is_null() {
    val parentTagId: Option[Long] = None
    val newTag = aTag.withId(1L).withName("newTag").build
    when(tagRepository.findOne(newTag.getTagId)).thenReturn(newTag)
    dut.moveToTag(newTag.getTagId, parentTagId)
    assertThat(newTag.isPooled, is(true))
  }

  @Test def processTags_최초_생성() {
    val java = Tag.pooledTag("java")
    val newTag = Tag.newTag("newTag")
    val plainTags = String.format("%s %s", java.getName, newTag.getName)
    when(tagRepository.findByName(java.getName)).thenReturn(java)
    when(tagRepository.save(newTag)).thenReturn(newTag)
    val tags = dut.processTags(plainTags)
    assertThat(tags.contains(java), is(true))
    assertThat(tags.contains(newTag), is(true))
  }

  @Test def processGroupTags() {
    val groupTag1 = new FacebookGroup("1234", "생활코딩")
    val groupTag2 = new FacebookGroup("5678", "slipp")
    val groupTag3 = new FacebookGroup("9123", "Na우리Next")

    val tag1 = Tag.groupedTag(groupTag1.getName, groupTag1.getGroupId)
    val tag2 = Tag.pooledTag(groupTag2.getName)
    val tag3 = Tag.groupedTag(groupTag3.getName, groupTag3.getGroupId)
    val newTags = Sets.newHashSet(groupTag1, groupTag2, groupTag3)

    when(tagRepository.findByGroupId(groupTag1.getGroupId)).thenReturn(tag1)
    when(tagRepository.findByGroupId(groupTag2.getGroupId)).thenReturn(null)
    when(tagRepository.findByName(groupTag2.getName)).thenReturn(Tag.pooledTag(groupTag2.getName))
    when(tagRepository.findByGroupId(groupTag3.getGroupId)).thenReturn(null)
    when(tagRepository.save(tag3)).thenReturn(tag3)

    val tags = dut.processGroupTags(newTags)
    log.debug(s"tags : ${tags}")

    assertThat(tags.contains(tag1), is(true))
    assertThat(tags.contains(tag2), is(true))
    assertThat(tags.contains(tag3), is(true))
  }

  @Test def parseTags_space() {
    val plainTags = "java javascript"
    val parsedTags = TagService.parseTags(plainTags)
    assertThat(parsedTags.size, is(2))
  }

  @Test def parseTags_comma() {
    var plainTags = "java,javascript"
    var parsedTags = TagService.parseTags(plainTags)
    assertThat(parsedTags.size, is(2))
    plainTags = "java,javascript,"
    parsedTags = TagService.parseTags(plainTags)
    assertThat(parsedTags.size, is(2))
  }
}
