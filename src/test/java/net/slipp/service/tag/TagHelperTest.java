package net.slipp.service.tag;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Set;

import net.slipp.domain.tag.Tag;

import org.junit.Test;

import com.google.common.collect.Sets;

public class TagHelperTest {

	@Test
	public void denormalizedTags() {
		Set<Tag> tags = Sets.newHashSet(Tag.newTag("java"), Tag.newTag("javascript"));
		String actual = TagHelper.denormalizedTags(tags);
		assertThat(actual, is("java,javascript"));
	}

}
