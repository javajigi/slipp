package net.slipp.domain.tag;

import java.util.Set;
import java.util.StringTokenizer;

import javax.inject.Inject;

import net.slipp.repository.tag.NewTagRepository;
import net.slipp.repository.tag.TagRepository;

import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

@Service
public class TagService {
	private TagRepository tagRepository;
	private NewTagRepository newTagRepository;
	
	@Inject
	public TagService(TagRepository tagRepository, NewTagRepository newTagRepository) {
		this.tagRepository = tagRepository;
		this.newTagRepository = newTagRepository;
	}
	
	public Set<Tag> processTags(String plainTags) {
		Set<String> parsedTags = parseTags(plainTags);
		Set<Tag> pooledTags = Sets.newHashSet();
		for (String parsedTag : parsedTags) {
			Tag tag = tagRepository.findByName(parsedTag);
			if(tag != null) {
				pooledTags.add(tagRepository.findByName(parsedTag));				
			} else {
				saveNewTag(parsedTag);
			}
		}
		return pooledTags;
	}
	
	private void saveNewTag(String parsedTag) {
		NewTag newTag = newTagRepository.findByName(parsedTag);
		if(newTag==null) {
			newTagRepository.save(new NewTag(parsedTag));
		} else {
			newTag.tagged();
			newTagRepository.save(newTag);
		}
	}

	public Set<String> parseTags(String plainTags) {
		Set<String> parsedTags = Sets.newHashSet();
		StringTokenizer tokenizer = new StringTokenizer(plainTags, " ");
		while (tokenizer.hasMoreTokens()) {
			parsedTags.add(tokenizer.nextToken());
		}
		return parsedTags;
	}
}
