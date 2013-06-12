package net.slipp.service.tag;

import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.inject.Inject;

import net.slipp.domain.tag.Tag;
import net.slipp.repository.tag.TagRepository;
import net.slipp.service.MailService;

import org.springframework.data.domain.Page;
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
                Tag newTag = Tag.newTag(each);
                Tag persisted = tagRepository.save(newTag);
                tags.add(persisted);
            } else {
                tags.add(tag);
            }
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

    public Page<Tag> findPooledTags(Pageable page) {
        return tagRepository.findByPooledTag(page);
    }

    public Page<Tag> findAllTags(Pageable page) {
        return tagRepository.findAll(page);
    }

    public Iterable<Tag> findPooledTags() {
        return tagRepository.findPooledParents();
    }

    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }
    
    public Tag requestNewTag(Tag tag) {
        Tag existedTag = tagRepository.findByName(tag.getName());
        if (existedTag != null) {
            throw new IllegalArgumentException(tag.getName() + " is already existed tag.");
        }
        
        Tag savedTag = tagRepository.save(tag);
        mailService.sendNewTagInformation(tag);
        return savedTag;
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
}
