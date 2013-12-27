package net.slipp.service.smalltalk;

import static org.mockito.Mockito.*;
import net.slipp.domain.smalltalk.SmallTalk;
import net.slipp.domain.smalltalk.SmallTalkComment;
import net.slipp.repository.smalltalk.SmallTalkCommentRepository;
import net.slipp.repository.smalltalk.SmallTalkRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RunWith(MockitoJUnitRunner.class)
public class SmallTalkServiceTest {

	@InjectMocks
	private SmallTalkService dut = new SmallTalkService();

	@Mock
	private SmallTalkRepository smallTalkRepository;

	@Mock
	private SmallTalkCommentRepository smallTalkCommentRepository;
	
	private SmallTalk smallTalk = null;

	@Before
	public void setUp() {
		smallTalk = new SmallTalk();
		smallTalk.setTalk("퍼시픽림 재밌다!!");
	}

	@Test
	public void testSave() {
		when(smallTalkRepository.save(smallTalk)).thenReturn(smallTalk);
		dut.create(smallTalk);
		verify(smallTalkRepository).save(smallTalk);
	}

	@Test
	public void testGetLastTalks() {
		Page<SmallTalk> page = smallTalkRepository.findAll(new PageRequest(0, 10));
		Assert.assertNull(page);
	}
	
	@Test
	public void testCreateComment(){
		Long smallTalkId = 0L;
		SmallTalkComment smallTalkComment = new SmallTalkComment();
		when(smallTalkRepository.findOne(smallTalkId)).thenReturn(smallTalk);
		dut.createComment(0L, smallTalkComment);
		verify(smallTalkCommentRepository, times(1)).save(smallTalkComment);
	}

}
