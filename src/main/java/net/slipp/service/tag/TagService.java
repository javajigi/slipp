package net.slipp.service.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.inject.Inject;

import net.slipp.domain.fb.FacebookGroup;
import net.slipp.domain.qna.Question;
import net.slipp.domain.tag.Tag;
import net.slipp.domain.tag.TaggedHistory;
import net.slipp.domain.tag.TaggedType;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.tag.TagRepository;
import net.slipp.repository.tag.TaggedHistoryRepository;
import net.slipp.service.MailService;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Sets;

@Service
@Transactional
public class TagService {
    @Resource(name = "mailService")
    private MailService mailService;
    
    private TagRepository tagRepository;
    
    @Resource(name="taggedHistoryRepository")
    private TaggedHistoryRepository taggedHistoryRepository;
    
    public TagService() {
    }

    @Inject
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Set<Tag> processTags(String plainTags) {
        Set<String> parsedTags = parseTags(plainTags);
        Set<Tag> tags = Sets.newHashSet();
        for (String each : parsedTags) {
            Tag tag = tagRepository.findByName(each);
            if (tag == null) {
                tags.add(newTag(each));
            } else {
                tags.add(tag);
            }
        }
        return tags;
    }
    
    public Tag newTag(String name) {
    	Tag newTag = Tag.newTag(name);
    	tagRepository.save(newTag);
    	return newTag;
    }
    
    public Set<Tag> processGroupTags(Set<FacebookGroup> groupTags) {
        Set<Tag> tags = Sets.newHashSet();
        for (FacebookGroup each : groupTags) {
            if (each.isEmpty()) {
                continue;
            }
            
            Tag tag = tagRepository.findByGroupId(each.getGroupId());
            if (tag != null) {
                tags.add(tag);
                continue;
            }
            
            tag = tagRepository.findByName(each.getName());
            if (tag != null) {
                tag.moveGroupTag(each.getGroupId());
                tags.add(tag);
                continue;
            }
            
            Tag newTag = Tag.groupedTag(each.getName(), each.getGroupId());
            tags.add(tagRepository.save(newTag));
        }
        return tags;
    }

    static Set<String> parseTags(String plainTags) {
        Set<String> parsedTags = Sets.newHashSet();
        StringTokenizer tokenizer = new StringTokenizer(plainTags, " |,");
        while (tokenizer.hasMoreTokens()) {
            parsedTags.add(tokenizer.nextToken());
        }
        return parsedTags;
    }
    
    public void moveToTag(Long newTagId, Long parentTagId) {
        Assert.notNull(newTagId, "이동할 tagId는 null이 될 수 없습니다.");

        Tag parentTag = findParentTag(parentTagId);
        Tag tag = tagRepository.findOne(newTagId);
        tag.movePooled(parentTag);
    }

    private Tag findParentTag(Long parentTagId) {
        if (parentTagId == null) {
            return null;
        }

        return tagRepository.findOne(parentTagId);
    }

    public Page<Tag> findAllTags(Pageable page) {
        return tagRepository.findAll(page);
    }

    public Iterable<Tag> findPooledParentTags() {
        return tagRepository.findPooledParents();
    }
    
    @Cacheable(value="latestTags")
    public Iterable<Tag> findLatestTags() {
    	PageRequest pageable = new PageRequest(0, 30);
    	Page<Object[]> page = taggedHistoryRepository.findsLatest(pageable);
    	List<Tag> latestTags = new ArrayList<Tag>();
    	for (Object[] objects : page) {
    		Tag tag = findTagById((Long)objects[0]);
    		if (tag.isTagged()) {
    			latestTags.add(tag);
    		}
		}
    	return latestTags;
    }

    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }
    
    public Tag findTagByName(String name) {
        return tagRepository.findByName(name);
    }

    public Tag findTagById(Long id) {
        return tagRepository.findOne(id);
    }

    public List<Tag> findsBySearch(String keyword) {
        return tagRepository.findByNameLike(keyword + "%");
    }
    
    public void saveTaggedHistories(Question question, Set<Tag> tags) {
    	for (Tag tag : tags) {
    		saveTaggedHistory(question.getWriter(), question, tag, TaggedType.TAGGED);
		}
    }
    
    public void saveTaggedHistory(SocialUser loginUser, Question question, Tag tag, TaggedType taggedType) {
    	taggedHistoryRepository.save(new TaggedHistory(tag.getTagId(), question.getQuestionId(), loginUser.getId(), taggedType));
    }
}
