package net.slipp.service.smalltalk;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.slipp.domain.smalltalk.SmallTalk;
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

	private SmallTalk smallTalk = null;

	@Before
	public void setUp() {
		smallTalk = new SmallTalk();
		smallTalk.setTalk("퍼시픽림 재밌다!!");
	}

	@Test
	public final void testSave() throws Exception {
		when(smallTalkRepository.save(smallTalk)).thenReturn(smallTalk);
		dut.save(smallTalk);
		verify(smallTalkRepository).save(smallTalk);
	}

	@Test
	public final void testGetLastTalks() throws Exception {
		Page<SmallTalk> page = smallTalkRepository.findAll(new PageRequest(0, 10));
		Assert.assertNull(page);
	}

}
