package net.slipp.repository.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import net.slipp.domain.tag.Tag;
import net.slipp.repository.tag.TagRepository;

public class MockTagRepository implements TagRepository {
	@SuppressWarnings("serial")
	Map<String, Tag> tags = new HashMap<String, Tag>() {{
		put("java", new Tag("java"));
		put("javascript", new Tag("javascript"));
		put("html", new Tag("html"));
		put("tdd", new Tag("tdd"));
		put("eclipse", new Tag("eclipse"));
		put("maven", new Tag("maven"));
	}};
	
	@Override
	public Tag findByName(String name) {
		return tags.get(name);
	}
	
	@Override
	public List<Tag> findByNameLike(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Tag save(Tag entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Tag> save(Iterable<? extends Tag> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tag findOne(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<Tag> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Tag entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Iterable<? extends Tag> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterable<Tag> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Tag> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}
}
