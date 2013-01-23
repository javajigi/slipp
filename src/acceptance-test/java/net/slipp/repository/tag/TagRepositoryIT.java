package net.slipp.repository.tag;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import net.slipp.domain.tag.Tag;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-applicationContext.xml")
public class TagRepositoryIT {
	@Autowired
	private TagRepository tagRepository;
	
	@Test
	public void findParents() throws Exception {
		Tag parent1 = new Tag("parent1");
		tagRepository.save(parent1);
		tagRepository.save(new Tag("child1", parent1));
		tagRepository.save(new Tag("child2", parent1));
		tagRepository.save(new Tag("parent2"));
		
		List<Tag> tags = tagRepository.findParents();
		assertThat(tags.size(), is(2));
	}
}
