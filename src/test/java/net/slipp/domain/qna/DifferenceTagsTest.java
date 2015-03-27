package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;

import net.slipp.domain.tag.Tag;

import org.junit.Test;

import com.google.common.collect.Sets;

public class DifferenceTagsTest {
	@Test
	public void differenceTag() throws Exception {
		Set<Tag> oldTags = Sets.newHashSet(Tag.newTag("java"), Tag.newTag("javascript"));
		Set<Tag> newTags = Sets.newHashSet(Tag.newTag("java"), Tag.newTag("자바"));
		DifferenceTags tags = new DifferenceTags(oldTags, newTags);
		
		Set<Tag> taagedNewTags = Sets.newHashSet(Tag.newTag("자바"));
		Set<Tag> detaggedOldTags = Sets.newHashSet(Tag.newTag("javascript"));
		assertThat(tags.taggedNewTags(), is(taagedNewTags));
		assertThat(tags.detaggedOldTags(), is(detaggedOldTags));
	}
}
