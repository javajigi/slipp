package net.slipp.service.smalltalk;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import net.slipp.domain.smallTalk.SmallTalk;
import net.slipp.repository.smalltalk.SmallTalkRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.google.common.collect.Lists;

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
		List<SmallTalk> smallTalks = Lists.newArrayList();
		PageImpl<SmallTalk> p = new PageImpl<SmallTalk>(smallTalks);
		when(smallTalkRepository.findAll(new PageRequest(0, 10))).thenReturn(p);
		
		dut.getLastTalks();
	}

}
