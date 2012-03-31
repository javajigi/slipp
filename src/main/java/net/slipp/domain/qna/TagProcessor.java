package net.slipp.domain.qna;

import java.util.Set;
import java.util.StringTokenizer;

import net.slipp.repository.qna.TagRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class TagProcessor {
	private static Logger logger = LoggerFactory.getLogger(TagProcessor.class);
	
	public Set<String> parseTags(String plainTags) {
		Set<String> parsedTags = Sets.newHashSet();
		StringTokenizer tokenizer = new StringTokenizer(plainTags, " ");
		while (tokenizer.hasMoreTokens()) {
			parsedTags.add(tokenizer.nextToken());
		}
		return parsedTags;
	}
	
	public Set<Tag> loadTags(Set<String> parsedTags, TagRepository tagRepository) {
		Set<Tag> tags = Sets.newHashSet();
		for (String parsedTag : parsedTags) {
			tags.add(tagRepository.findByName(parsedTag));
		}
		return tags;
	}
	
	public void removeTags(Set<Tag> newTags, Set<Tag> orginalTags) {
		SetView<Tag> removedTags = Sets.difference(orginalTags, newTags);
		logger.debug("removedTags size : {}", removedTags.size());
		for (Tag tag : removedTags) {
			tag.deTagged();
		}
	}

	public void addNewTags(Set<Tag> newTags, Set<Tag> orginalTags) {
		SetView<Tag> addedTags = Sets.difference(newTags, orginalTags);
		logger.debug("addedTags size : {}", addedTags.size());
		for (Tag tag : addedTags) {
			tag.tagged();
		}
	}
}
