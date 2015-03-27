package net.slipp.domain.qna;

import java.util.Set;

import net.slipp.domain.tag.Tag;

import com.google.common.collect.Sets;

public class DifferenceTags {
	private Set<Tag> oldTags;
	private Set<Tag> newTags;

	public DifferenceTags(Set<Tag> oldTags, Set<Tag> newTags) {
		this.oldTags = oldTags;
		this.newTags = newTags;
	}

	public Set<Tag> taggedNewTags() {
		return Sets.difference(newTags, oldTags);
	}

	public Set<Tag> detaggedOldTags() {
		return Sets.difference(oldTags, newTags);
	}
}
