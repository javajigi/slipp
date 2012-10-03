package net.slipp.domain.tag;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import net.slipp.domain.qna.Question;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.tag.NewTagRepository;
import net.slipp.repository.tag.TagRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	public void saveNewTag(SocialUser loginUser, Question question, Set<NewTag> newTags) {
		for (NewTag newTag : newTags) {
			NewTag originalTag = newTagRepository.findByName(newTag.getName());
			
			if(originalTag==null) {
				newTag.addUser(loginUser);
				newTag.addQuestion(question);
				newTagRepository.save(newTag);
			} else {
				originalTag.addUser(loginUser);
				originalTag.addQuestion(question);				
				originalTag.tagged();
			}			
		}
	}

	public void moveToTagPool(Long tagId, Long parentTagId) {
		Tag parentTag = null;
		if (parentTagId != null) {
			parentTag = tagRepository.findOne(parentTagId);
		}
		
		NewTag newTag = newTagRepository.findOne(tagId);
		Tag tag = tagRepository.findByName(newTag.getName());
		if (tag == null) {
			tagRepository.save(newTag.createTag(parentTag));
		}
		newTag.deleted();
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
