package net.slipp.domain.tag;

import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.inject.Inject;

import net.slipp.repository.tag.NewTagRepository;
import net.slipp.repository.tag.TagRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;

@Service
@Transactional
public class TagService {
	private TagRepository tagRepository;
	private NewTagRepository newTagRepository;
	
	public TagService() {
	}
	
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
				pooledTags.add(tag.getRevisedTag());				
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

	public void moveToTagPool(Long tagId) {
		NewTag newTag = newTagRepository.findOne(tagId);
		
		Tag tag = tagRepository.findByName(newTag.getName());
		if (tag == null) {
			tagRepository.save(newTag.createTag());
			newTagRepository.delete(newTag);
		}
	}
	
	public Page<Tag> findTags(Pageable page) {
		return tagRepository.findAll(page);
	}
	
	public List<Tag> findParents() {
		return tagRepository.findParents();
	}
	
	public Page<NewTag> findNewTags(Pageable page) {
		return newTagRepository.findAll(page);
	}
	
	public void saveTag(Tag tag) {
		tagRepository.save(tag);
	}
	
	public Tag findTagByName(String name) {
		return tagRepository.findByName(name);
	}
	
	public Tag findTagById(Long id) {
		return tagRepository.findOne(id);
	}
}
