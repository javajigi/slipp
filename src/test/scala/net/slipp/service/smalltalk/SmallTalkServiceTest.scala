package net.slipp.service.smalltalk

import org.mockito.Mockito._
import net.slipp.ndomain.smalltalk.SmallTalk
import net.slipp.ndomain.smalltalk.SmallTalkComment
import net.slipp.repository.smalltalk.SmallTalkCommentRepository
import net.slipp.repository.smalltalk.SmallTalkRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

@RunWith(classOf[MockitoJUnitRunner]) class SmallTalkServiceTest {
  @InjectMocks private var dut: SmallTalkService = new SmallTalkService
  @Mock private var smallTalkRepository: SmallTalkRepository = null
  @Mock private var smallTalkCommentRepository: SmallTalkCommentRepository = null
  private var smallTalk: SmallTalk = null

  @Before def setUp {
    smallTalk = new SmallTalk
    smallTalk.setTalk("퍼시픽림 재밌다!!")
  }

  @Test def testSave {
    when(smallTalkRepository.save(smallTalk)).thenReturn(smallTalk)
    dut.create(smallTalk)
    verify(smallTalkRepository).save(smallTalk)
  }

  @Test def testGetLastTalks {
    val page: Page[SmallTalk] = smallTalkRepository.findAll(new PageRequest(0, 10))
    Assert.assertNull(page)
  }

  @Test def testCreateComment {
    val smallTalkId: Long = 0L
    val smallTalkComment: SmallTalkComment = new SmallTalkComment
    when(smallTalkRepository.findOne(smallTalkId)).thenReturn(smallTalk)
    dut.createComment(0L, smallTalkComment)
    verify(smallTalkCommentRepository, times(1)).save(smallTalkComment)
  }
}
