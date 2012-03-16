package net.slipp.qna;

import java.util.Set;
import java.util.StringTokenizer;

import com.google.common.collect.Sets;

public class TagFinder {
	private String tags;

	public TagFinder(String tags) {
		this.tags = tags;
	}

	public Set<Tag> parseTags() {
		StringTokenizer tokenizer = new StringTokenizer(tags, " ");
		Set<Tag> parsedTags = Sets.newHashSet();
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			parsedTags.add(new Tag(token));
		}
		return parsedTags;
	}

}
