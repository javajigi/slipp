package net.slipp.service.tag;

import java.util.Set;
import java.util.StringTokenizer;

import net.slipp.domain.tag.NewTag;
import net.slipp.domain.tag.Tag;
import net.slipp.repository.tag.TagRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class TagProcessor {
	private static final Logger logger = LoggerFactory.getLogger(TagProcessor.class);
	
	private TagRepository tagRepository;
	
	private Set<Tag> tags = Sets.newHashSet();
	private Set<NewTag> newTags = Sets.newHashSet();
	private String denormalizedTags;
	
	public TagProcessor(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	public void processTags(String plainTags) {
		Set<String> parsedTags = parseTags(plainTags);
		for (String parsedTag : parsedTags) {
			applyParsedTag(parsedTag);
		}
	}

	private void applyParsedTag(String parsedTag) {
		Tag tag = tagRepository.findByName(parsedTag);
		if(tag != null) {
			tags.add(tag.getRevisedTag());				
		} else {
			newTags.add(new NewTag(parsedTag));
		}
	}
	
	public void processTags(Set<Tag> originalTags, String plainTags) {
		Set<String> parsedTags = parseTags(plainTags);
		for (String parsedTag : parsedTags) {
			applyParsedTag(parsedTag);
		}
		addNewTags(tags, originalTags);
		removeTags(tags, originalTags);
		this.denormalizedTags = tagsToDenormalizedTags(tags);
	}
	
	private void addNewTags(Set<Tag> newTags, Set<Tag> orginalTags) {
		SetView<Tag> addedTags = Sets.difference(newTags, orginalTags);
		logger.debug("addedTags size : {}", addedTags.size());
		for (Tag tag : addedTags) {
			tag.tagged();
		}
	}
	
	private void removeTags(Set<Tag> newTags, Set<Tag> orginalTags) {
		SetView<Tag> removedTags = Sets.difference(orginalTags, newTags);
		logger.debug("removedTags size : {}", removedTags.size());
		for (Tag tag : removedTags) {
			tag.deTagged();
		}
	}
	
	public static Set<String> parseTags(String plainTags) {
		Set<String> parsedTags = Sets.newHashSet();
		StringTokenizer tokenizer = new StringTokenizer(plainTags, " ");
		while (tokenizer.hasMoreTokens()) {
			parsedTags.add(tokenizer.nextToken());
		}
		return parsedTags;
	}
	
	public static String tagsToDenormalizedTags(Set<Tag> tags) {
		Function<Tag, String> tagToString = new Function<Tag, String>() {
			@Override
			public String apply(Tag input) {
				return input.getName();
			}
		};
		
		return Joiner.on(",").join(Collections2.transform(tags, tagToString));
	}
	
	public Set<Tag> getTags() {
		return tags;
	}
	
	public Set<NewTag> getNewTags() {
		return newTags;
	}
	
	public String getDenormalizedTags() {
		return denormalizedTags;
	}
}
