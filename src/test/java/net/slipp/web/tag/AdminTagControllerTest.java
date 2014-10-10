package net.slipp.web.tag;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import net.slipp.domain.tag.Tag;
import net.slipp.service.tag.TagService;
import net.slipp.web.tag.AdminTagController;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.ModelMap;

@RunWith(MockitoJUnitRunner.class)
public class AdminTagControllerTest {
	@Mock
	private TagService tagService;
	
	@InjectMocks
	private AdminTagController dut = new AdminTagController();

	@Test
	public void tags_페이지없음() throws Exception {
		Pageable pageable = dut.createPageable(null);
		List<Tag> tags = Arrays.asList(Tag.pooledTag("java"), Tag.pooledTag("svn"));
		Page<Tag> pageTags = new PageImpl<Tag>(tags, pageable, 2L);
		
		when(tagService.findAllTags(dut.createPageable(null))).thenReturn(pageTags);
		
		ModelMap model = new ModelMap();
		String forwardUrl = dut.tags(null, model);
		assertThat(forwardUrl, is("admin/tags"));
		assertThat(model.get("tags"), is(notNullValue()));
	}
}
