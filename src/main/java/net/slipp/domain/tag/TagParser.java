package net.slipp.domain.tag;

import java.util.Set;
import java.util.StringTokenizer;

import net.slipp.repository.tag.TagRepository;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

public class TagParser {
	private TagRepository tagRepository;
	
	private Set<Tag> pooledTags = Sets.newHashSet();
	private Set<NewTag> newTags = Sets.newHashSet();
	
	public TagParser(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	Set<String> parseTags(String plainTags) {
		Set<String> parsedTags = Sets.newHashSet();
		StringTokenizer tokenizer = new StringTokenizer(plainTags, " ");
		while (tokenizer.hasMoreTokens()) {
			parsedTags.add(tokenizer.nextToken());
		}
		return parsedTags;
	}
	
	public void processTags(String plainTags) {
		Set<String> parsedTags = parseTags(plainTags);
		for (String parsedTag : parsedTags) {
			Tag tag = tagRepository.findByName(parsedTag);
			if(tag != null) {
				pooledTags.add(tag.getRevisedTag());				
			} else {
				newTags.add(new NewTag(parsedTag));
			}
		}
	}
	
	public String tagsToDenormalizedTags(Set<Tag> tags) {
		Function<Tag, String> tagToString = new Function<Tag, String>() {
			@Override
			public String apply(Tag input) {
				return input.getName();
			}
		};
		
		return Joiner.on(",").join(Collections2.transform(tags, tagToString));
	}
	
	public Set<Tag> getPooledTags() {
		return pooledTags;
	}
	
	public Set<NewTag> getNewTags() {
		return newTags;
	}
}
