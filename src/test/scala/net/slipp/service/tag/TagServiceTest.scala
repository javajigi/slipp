package net.slipp.service.tag

import java.lang.Long
import java.util.Set

import com.google.common.collect.Sets
import net.slipp.domain.fb.FacebookGroup
import net.slipp.domain.tag.Tag
import net.slipp.domain.tag.TagBuilder.aTag
import net.slipp.repository.tag.TagRepository
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.junit.{Before, Test}
import org.junit.runner.RunWith
import org.mockito.{InjectMocks, Mock}
import org.mockito.Mockito.when
import org.mockito.runners.MockitoJUnitRunner

@RunWith(classOf[MockitoJUnitRunner])
class TagServiceTest {
  @Mock private var tagRepository: TagRepository = null
  @InjectMocks private val dut = new TagService

  @Test def moveToTag_부모_태그_ID_is_null {
    val parentTagId: Option[Long] = None
    val newTag: Tag = aTag.withId(1L).withName("newTag").build
    when(tagRepository.findOne(newTag.getTagId)).thenReturn(newTag)
    dut.moveToTag(newTag.getTagId, parentTagId)
    assertThat(newTag.isPooled, is(true))
  }

  @Test def processTags_최초_생성 {
    val java: Tag = Tag.pooledTag("java")
    val newTag: Tag = Tag.newTag("newTag")
    val plainTags: String = String.format("%s %s", java.getName, newTag.getName)
    when(tagRepository.findByName(java.getName)).thenReturn(java)
    when(tagRepository.save(newTag)).thenReturn(newTag)
    val tags: Set[Tag] = dut.processTags(plainTags)
    assertThat(tags.contains(java), is(true))
    assertThat(tags.contains(newTag), is(true))
  }

  @Test def processGroupTags {
    val groupTag1: FacebookGroup = new FacebookGroup("1234", "생활코딩")
    val groupTag2: FacebookGroup = new FacebookGroup("5678", "slipp")
    val groupTag3: FacebookGroup = new FacebookGroup("9123", "Na우리Next")

    val tag1: Tag = Tag.groupedTag(groupTag1.getName, groupTag1.getGroupId)
    val tag2: Tag = Tag.pooledTag(groupTag2.getName)
    val tag3: Tag = Tag.groupedTag(groupTag3.getName, groupTag3.getGroupId)
    val newTags: Set[FacebookGroup] = Sets.newHashSet(groupTag1, groupTag2, groupTag3)

    when(tagRepository.findByGroupId(groupTag1.getGroupId)).thenReturn(tag1)
    when(tagRepository.findByGroupId(groupTag2.getGroupId)).thenReturn(null)
    when(tagRepository.findByName(groupTag2.getName)).thenReturn(Tag.pooledTag(groupTag2.getName))
    when(tagRepository.findByGroupId(groupTag3.getGroupId)).thenReturn(null)
    when(tagRepository.save(tag3)).thenReturn(tag3)

    val tags: Set[Tag] = dut.processGroupTags(newTags)

    assertThat(tags.contains(tag1), is(true))
    assertThat(tags.contains(tag2), is(true))
    assertThat(tags.contains(tag3), is(true))
  }

  @Test
  @throws(classOf[Exception])
  def parseTags_space {
    val plainTags: String = "java javascript"
    val parsedTags: Set[String] = TagService.parseTags(plainTags)
    assertThat(parsedTags.size, is(2))
  }

  @Test
  @throws(classOf[Exception])
  def parseTags_comma {
    var plainTags: String = "java,javascript"
    var parsedTags: Set[String] = TagService.parseTags(plainTags)
    assertThat(parsedTags.size, is(2))
    plainTags = "java,javascript,"
    parsedTags = TagService.parseTags(plainTags)
    assertThat(parsedTags.size, is(2))
  }
}
